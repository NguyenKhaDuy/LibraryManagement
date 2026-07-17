import { api, submitJson } from "../core/api.js";
import { els } from "../core/dom.js";
import { state } from "../core/state.js";
import { enc, errorText, esc, firstImage, bookImages, get, normalizeRows, query, statusClass, statusText, unwrapData, valueOf } from "../core/utils.js";
import { bookFields, mapBookToForm } from "../config/fields.js";
import { bindImagePreviews, fieldInline, formDataValues, formHtml, selectInline } from "../ui/forms.js";
import { toast } from "../ui/feedback.js";
import { openDrawer, closeDrawer } from "../ui/drawer.js";
import { detailGrid } from "../ui/table.js";
import { errorHtml, setTitle } from "../ui/view.js";

export async function renderBooks(readerMode) {
    setTitle(readerMode ? "Tra cứu sách" : "Kho sách", readerMode ? "Dành cho độc giả" : "Quản lý danh mục");
    let publisherOptions = [];
    let authorOptions = [];
    let categoryOptions = [];
    let shelfOptions = [];

    try {
        // Nhà xuất bản
        const pubData = await api("/api/admin/publishing-houses");
        publisherOptions = normalizeRows(pubData).map(function (item) {
            return {
                value: item.idPublishingHouse,
                label: item.name || ("NXB #" + item.idPublishingHouse)
            };
        });

        // Tác giả
        const authorData = await api("/api/admin/authors");
        authorOptions = normalizeRows(authorData).map(function (item) {
            return {
                value: item.idAuthor,
                label: item.fullName || ("TG #" + item.idAuthor)
            };
        });

        // Thể loại
        const categoryData = await api("/api/category");
        categoryOptions = normalizeRows(categoryData).map(function (item) {
            return {
                value: item.idCategory,
                label: item.nameCategory || ("TL #" + item.idCategory)
            };
        });

        // Kệ sách
        const shelfData = await api("/api/admin/book-shelfs");
        shelfOptions = normalizeRows(shelfData).map(function (item) {
            return {
                value: item.idBookshelf,
                label: item.location || ("Kệ #" + item.idBookShelf)
            };
        });

    } catch (err) {
        publisherOptions = [];
        authorOptions = [];
        categoryOptions = [];
        shelfOptions = [];
    }
    els.pageRoot.innerHTML = [
        '<section class="surface">',
        '<div class="section-header">',
        '<div class="section-title"><h2>' + (readerMode ? "Tìm sách để mượn" : "Danh sách đầu sách") + '</h2><p>Lọc theo tác giả, thể loại, kệ hoặc nhà xuất bản.</p></div>',
        '<div class="button-row">' + (!readerMode ? '<button class="button button-primary" id="newBookButton" type="button">Thêm sách</button>' : "") + '</div>',
        '</div>',
        '<div class="toolbar">',
        fieldInline("bookSearch", "Tìm nhanh", "text"),
        selectInline("filterAuthor", "Tác giả", authorOptions),
        selectInline("filterCategory", "Thể loại", categoryOptions),
        selectInline("filterShelf", "Kệ", shelfOptions),
        selectInline("filterPublisher", "Nhà xuất bản", publisherOptions),
        '<button class="button button-secondary" id="bookFilterButton" type="button">Lọc</button>',
        '</div>',
        '<div class="record-list" id="booksGrid"><div class="empty-state">Đang tải sách...</div></div>',
        '<div class="section-header"><div class="pill-row" id="bookPager"></div><div class="button-row"><button class="button button-secondary" id="bookPrev" type="button">Trang trước</button><button class="button button-secondary" id="bookNext" type="button">Trang sau</button></div></div>',
        '</section>'
    ].join("");
    if (!readerMode) {
        document.getElementById("newBookButton").addEventListener("click", function () { openBookForm(null); });
    }
    document.getElementById("bookFilterButton").addEventListener("click", function () {
        state.pageNo.books = 1;
        loadBooks(readerMode);
    });
    ["bookSearch", "filterAuthor", "filterCategory", "filterShelf", "filterPublisher"].forEach(function (id) {
        document.getElementById(id).addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                state.pageNo.books = 1;
                loadBooks(readerMode);
            }
        });
    });
    document.getElementById("bookPrev").addEventListener("click", function () {
        state.pageNo.books = Math.max(1, (state.pageNo.books || 1) - 1);
        loadBooks(readerMode);
    });
    document.getElementById("bookNext").addEventListener("click", function () {
        state.pageNo.books = (state.pageNo.books || 1) + 1;
        loadBooks(readerMode);
    });
    await loadBooks(readerMode);
}

async function loadBooks(readerMode) {
    const pageNo = state.pageNo.books || 1;
    const params = { pageNo: pageNo };
    const author = valueOf("filterAuthor");
    const category = valueOf("filterCategory");
    const shelf = valueOf("filterShelf");
    const publisher = valueOf("filterPublisher");
    if (author) params.idAuthor = author;
    if (category) params.idCategory = category;
    if (shelf) params.idBookShelf = shelf;
    if (publisher) params.idPublisher = publisher;
    const grid = document.getElementById("booksGrid");
    grid.innerHTML = '<div class="empty-state">Đang tải sách...</div>';
    try {
        const data = await api("/api/book" + query(params));
        let rows = normalizeRows(data);
        const keyword = valueOf("bookSearch").toLowerCase();
        if (keyword) {
            rows = rows.filter(function (book) {
                return [book.idBook, book.nameBook, get(book, "authorDTO.fullName"), get(book, "categoryDTO.nameCategory")]
                    .join(" ").toLowerCase().indexOf(keyword) >= 0;
            });
        }
        state.rows = rows;
        grid.innerHTML = rows.length ? rows.map(function (book, index) {
            return bookCard(book, index, readerMode);
        }).join("") : '<div class="empty-state">Không có sách phù hợp.</div>';
        grid.querySelectorAll("[data-book-action]").forEach(function (button) {
            button.addEventListener("click", function (event) {
                event.stopPropagation();
                const book = rows[Number(button.dataset.index)];
                if (button.dataset.bookAction === "cart") {
                    addBookToCart(book);
                } else if (button.dataset.bookAction === "edit") {
                    openBookForm(book);
                } else if (button.dataset.bookAction === "delete") {
                    deleteBook(book);
                }
            });
        });
        grid.querySelectorAll("[data-book-detail]").forEach(function (card) {
            card.addEventListener("click", function () {
                openBookDetail(rows[Number(card.dataset.bookDetail)], readerMode);
            });
        });
        const pager = document.getElementById("bookPager");
        pager.innerHTML = '<span class="pill">Trang ' + esc(data.current_page || pageNo) + ' / ' + esc(data.total_pages || 1) + '</span><span class="pill pill-warning">' + rows.length + ' sách</span>';
        document.getElementById("bookPrev").disabled = pageNo <= 1;
        document.getElementById("bookNext").disabled = data.total_pages ? pageNo >= data.total_pages : rows.length === 0;
    } catch (err) {
        grid.innerHTML = errorHtml(err);
    }
}

function bookCard(book, index, readerMode) {
    const image = firstImage(book);
    const status = book.status || "AVAILABLE";
    const canBorrow = readerMode && status !== "REMOVED" && Number(book.quantity || 0) > 0;
    const actions = readerMode
        ? '<button class="button button-primary" data-book-action="cart" data-index="' + index + '" type="button"' + (canBorrow ? "" : " disabled") + '>Thêm vào giỏ</button>'
        : '<div class="button-row"><button class="button button-secondary" data-book-action="edit" data-index="' + index + '" type="button">Sửa</button><button class="button button-danger" data-book-action="delete" data-index="' + index + '" type="button">Xóa</button></div>';
    return [
        '<article class="book-card" data-book-detail="' + index + '">',
        '<div class="book-cover">' + (image ? '<img src="' + image + '" alt="">' : esc((book.nameBook || "S").slice(0, 1))) + '</div>',
        '<div class="pill-row"><span class="' + statusClass(status) + '">' + esc(statusText(status)) + '</span><span class="pill">' + esc(book.quantity || 0) + ' quyển</span></div>',
        '<h3>' + esc(book.nameBook || "Chưa đặt tên") + '</h3>',
        '<div class="meta-list"><span>' + esc(get(book, "authorDTO.fullName") || "Chưa có tác giả") + '</span><span>' + esc(get(book, "categoryDTO.nameCategory") || "Chưa phân loại") + '</span></div>',
        actions,
        '</article>'
    ].join("");
}

async function openBookDetail(book, readerMode) {
    openDrawer("Chi tiết sách", book.nameBook || book.idBook || "Kho sách", '<div class="empty-state">Đang tải chi tiết sách...</div>');
    try {
        const detail = unwrapData(await api("/api/book/id-book=" + enc(book.idBook)));
        renderBookDetail(detail, readerMode);
    } catch (err) {
        openDrawer("Chi tiết sách", book.nameBook || book.idBook || "Kho sách", errorHtml(err));
    }
}

function renderBookDetail(book, readerMode) {
    const images = bookImages(book);
    const coverHtml = images.length
        ? '<div class="image-gallery">' + images.map(function (src) { return '<img src="' + src + '" alt="">'; }).join("") + "</div>"
        : '<div class="book-cover detail-cover">' + esc((book.nameBook || "S").slice(0, 1)) + "</div>";
    openDrawer("Chi tiết sách", "Kho sách", [
        '<div class="page-stack">',
        coverHtml,
        detailGrid([
            ["Mã sách", book.idBook], ["Tên sách", book.nameBook], ["Tác giả", get(book, "authorDTO.fullName")],
            ["Thể loại", get(book, "categoryDTO.nameCategory")], ["Kệ", get(book, "bookshelfDTO.location")],
            ["Nhà xuất bản", get(book, "publishingHouseDTO.name")], ["Số lượng", book.quantity], ["Trạng thái", book.status],
            ["Năm xuất bản", book.yearOfPub], ["Ngôn ngữ", book.language]
        ]),
        '<p class="muted">' + esc(book.description || "") + '</p>',
        '<div class="button-row">' + (readerMode ? '<button class="button button-primary" id="detailCartButton" type="button">Thêm vào giỏ</button>' : '<button class="button button-primary" id="detailEditButton" type="button">Sửa sách</button>') + '</div>',
        '</div>'
    ].join(""), function () {
        const cartButton = document.getElementById("detailCartButton");
        if (cartButton) cartButton.addEventListener("click", function () { addBookToCart(book); });
        const editButton = document.getElementById("detailEditButton");
        if (editButton) editButton.addEventListener("click", function () { openBookForm(book); });
    });
}

async function openBookForm(book) {
    const isEdit = Boolean(book);

    let publisherOptions = [];
    let authorOptions = [];
    let categoryOptions = [];
    let shelfOptions = [];

    try {
        // Nhà xuất bản
        const pubData = await api("/api/admin/publishing-house?pageNo=1");
        publisherOptions = normalizeRows(pubData).map(function (item) {
            return {
                value: item.idPublishingHouse,
                label: item.name || ("NXB #" + item.idPublishingHouse)
            };
        });

        // Tác giả
        const authorData = await api("/api/admin/author?pageNo=1");
        authorOptions = normalizeRows(authorData).map(function (item) {
            return {
                value: item.idAuthor,
                label: item.fullName || ("TG #" + item.idAuthor)
            };
        });

        // Thể loại
        const categoryData = await api("/api/category");
        categoryOptions = normalizeRows(categoryData).map(function (item) {
            return {
                value: item.idCategory,
                label: item.nameCategory || ("TL #" + item.idCategory)
            };
        });

        // Kệ sách
        const shelfData = await api("/api/admin/book-shelf?pageNo=1");
        shelfOptions = normalizeRows(shelfData).map(function (item) {
            return {
                value: item.idBookshelf,
                label: item.location || ("Kệ #" + item.idBookshelf)
            };
        });

    } catch (err) {
        publisherOptions = [];
        authorOptions = [];
        categoryOptions = [];
        shelfOptions = [];
    }

    let fields = isEdit
        ? [...bookFields]
        : bookFields.filter(function (f) {
            return f.name !== "idBook" && f.name !== "status";
        });

    // Đổi các trường ID thành select
    fields = fields.map(function (f) {

        if (f.name === "authorId") {
            return {
                ...f,
                options: authorOptions
            };
        }

        if (f.name === "categoryId") {
            return {
                ...f,
                options: categoryOptions
            };
        }

        if (f.name === "bookShelfId") {
            return {
                ...f,
                options: shelfOptions
            };
        }

        if (f.name === "publisherId") {
            return {
                ...f,
                options: publisherOptions
            };
        }

        if (f.name === "images") {
            return {
                ...f,
                multiple: true,
                accept: "image/*",
                existingImages: isEdit ? bookImages(book) : []
            };
        }

        return f;
    });

    openDrawer(
        isEdit ? "Cập nhật sách" : "Thêm sách",
        "Kho sách",
        formHtml(
            "bookForm",
            fields,
            isEdit ? mapBookToForm(book) : {},
            isEdit ? "Lưu thay đổi" : "Thêm sách"
        ),
        function () {

            const imageInput = document.querySelector("#bookForm input[name='images']");
            if (imageInput) {
                imageInput.multiple = true;
                imageInput.accept = "image/*";
            }

            bindImagePreviews(document.getElementById("bookForm"));

            document
                .getElementById("bookForm")
                .addEventListener("submit", async function (event) {

                    event.preventDefault();

                    await submitBookForm(
                        isEdit ? "PUT" : "POST",
                        event.currentTarget,
                        fields
                    );

                    closeDrawer();
                    loadBooks(false);
                });
        }
    );
}

async function submitBookForm(method, form, fields) {
    const formData = new FormData();

    fields.forEach(function (field) {
        const input = form.elements[field.name];
        if (!input) return;

        if (field.type === "file") {
            Array.from(input.files).forEach(function (file) {
                formData.append(field.name, file);
            });
        } else {
            formData.append(field.name, input.value);
        }
    });

    const result = await api("/api/admin/book", {
        method: method,
        body: formData
    });

    toast(result.message || "Đã lưu sách.", "success");
    return result;
}

async function deleteBook(book) {
    if (!confirm("Xóa sách " + (book.nameBook || book.idBook) + "?")) return;
    await submitJson("DELETE", "/api/admin/book/id-book=" + enc(book.idBook), null, "Đã xóa sách.");
    loadBooks(false);
}

async function addBookToCart(book) {
    try {
        const cartId = await currentCartId();
        await submitJson("POST", "/api/reader/cart", { idCart: cartId, idBook: book.idBook }, "Đã thêm sách vào giỏ.");
    } catch (err) {
        toast(errorText(err), "error");
    }
}

async function currentCartId() {
    const data = await api("/api/reader/cart/idReader=" + enc(state.session.idUser));
    const rows = normalizeRows(data);
    if (!rows.length || !rows[0].idCart) {
        throw new Error("Không tìm thấy giỏ mượn của tài khoản này.");
    }
    return rows[0].idCart;
}
