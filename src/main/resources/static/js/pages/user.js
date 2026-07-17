import { api, optionalApi, submitJson } from "../core/api.js";
import { els } from "../core/dom.js";
import { state } from "../core/state.js";
import { enc, errorText, esc, get, normalizeRows, unwrapData } from "../core/utils.js";
import { field } from "../config/fields.js";
import { fieldInline } from "../ui/forms.js";
import { toast } from "../ui/feedback.js";
import { openDrawer } from "../ui/drawer.js";
import { bindTableRows, detailGrid, detailItems, metric, tableHtml } from "../ui/table.js";
import { errorHtml, renderError, setTitle } from "../ui/view.js";
import { openBorrowForm } from "./circulation.js";

export async function renderReaderDashboard() {
    setTitle("Tổng quan", "Không gian độc giả");
    els.pageRoot.innerHTML = '<div class="page-stack"><div class="empty-state">Đang chuẩn bị không gian độc giả...</div></div>';
    try {
        const profileData = await optionalApi("/api/reader/idReader=" + enc(state.session.idUser), {});
        const loansData = await optionalApi("/api/user/borrow-ticket/idReader=" + enc(state.session.idUser), []);
        const cartData = await optionalApi("/api/reader/cart/idReader=" + enc(state.session.idUser), []);
        const booksData = await optionalApi("/api/book?pageNo=1", { data: [] });
        const profile = unwrapData(profileData) || {};
        const tickets = normalizeRows(loansData);
        const carts = normalizeRows(cartData);
        const suggestions = normalizeRows(booksData).slice(0, 4);
        const cartItems = collectCartItems(carts);
        const activeLoans = tickets.filter(function (ticket) {
            return ["COMPLETED", "RETURNED", "CANCELLED", "REJECTED"].indexOf(ticket.status) < 0;
        });
        const libraryCards = profile.libraryCardDTOS || [];
        const cardCount = libraryCards.length;

        els.pageRoot.innerHTML = [
            '<div class="page-stack reader-home">',
            '<section class="surface reader-hero">',

            '<div>',
            '<p class="eyebrow">Xin chào</p>',
            '<h2>' + esc(profile.fullName || state.session.username || "Độc giả") + '</h2>',
            '<p>Theo dõi sách đang mượn, giỏ mượn và tìm nhanh đầu sách phù hợp trong thư viện.</p>',

            '<div class="button-row">',
            '<button class="button button-primary" data-go="catalog" type="button">Tra cứu sách</button>',
            '<button class="button button-secondary" data-go="myLoans" type="button">Xem phiếu mượn</button>',
            '</div>',

            libraryCards.length
                ? '<div class="library-card-grid">' +
                libraryCards.map(function(card) {
                    return [
                        '<div class="library-card-item">',

                        '<div class="card-header">',
                        '<strong>Thẻ thư viện</strong>',
                        '<span class="card-status">' + esc(card.status || "") + '</span>',
                        '</div>',

                        '<div class="card-body">',
                        '<p><span>Mã thẻ</span><b>' + esc(card.idCard || "") + '</b></p>',
                        '<p><span>Ngày cấp</span><b>' + esc(card.dateOfIssue || "") + '</b></p>',
                        '<p><span>Hết hạn</span><b>' + esc(card.expirationDate || "") + '</b></p>',
                        '</div>',

                        '</div>'
                    ].join("");
                }).join("") +
                '</div>'
                : '<div class="empty-state">Chưa có thẻ thư viện</div>',

            '</div>',

            '</section>',
            '<div class="metrics-grid">',
            metric("Phiếu mượn đang xử lý", activeLoans.length, "green"),
            metric("Sách trong giỏ", cartItems.length, "amber"),
            metric("Thẻ thư viện", cardCount, "violet"),
            '</div>',
            '<div class="grid-2">',
            readerLoanPanel(tickets.slice(0, 6)),
            readerSuggestionPanel(suggestions),
            '</div>',
            '</div>'
        ].join("");

        els.pageRoot.querySelectorAll("[data-go]").forEach(function (button) {
            button.addEventListener("click", function () {
                window.dispatchEvent(new CustomEvent("ktt:navigate", { detail: button.dataset.go }));
            });
        });
    } catch (err) {
        renderError(err);
    }
}

export async function renderCart() {
    setTitle("Giỏ mượn", "Không gian độc giả");
    els.pageRoot.innerHTML = [
        '<section class="surface">',
        '<div class="section-header">',
        '<div class="section-title"><h2>Sách đang chọn</h2><p>Giữ lại những đầu sách bạn muốn gửi yêu cầu mượn.</p></div>',
        '<button class="button button-primary" id="cartBorrowButton" type="button">Tạo yêu cầu mượn</button>',
        '</div>',
        '<div class="record-list" id="cartItems"><div class="empty-state">Đang tải giỏ mượn...</div></div>',
        '</section>'
    ].join("");
    let cartBorrowLines = [];
    document.getElementById("cartBorrowButton").addEventListener("click", function () {
        openBorrowForm(false, renderMyLoans, null, cartBorrowLines);
    });
    try {
        const data = await api("/api/reader/cart/idReader=" + enc(state.session.idUser));
        const carts = normalizeRows(data);
        const items = collectCartItems(carts);
        cartBorrowLines = items.map(function (item) {
            return { bookId: item.idBook, statusBorrow: "BORROWING", fine: 0, note: item.nameBook || "" };
        });
        const container = document.getElementById("cartItems");
        container.innerHTML = items.length ? items.map(function (item, index) {
            return [
                '<article class="mini-card">',
                '<div class="book-cover compact-cover">' + esc((item.nameBook || item.idBook || "S").slice(0, 1)) + '</div>',
                '<h3>' + esc(item.nameBook || item.idBook || "Sách chưa rõ tên") + '</h3>',
                '<div class="meta-list"><span>' + esc(item.nameAuthor || "Chưa rõ tác giả") + '</span><span>' + esc(item.yearOfPub || "") + '</span></div>',
                '<button class="button button-danger" data-remove-cart="' + index + '" type="button">Bỏ khỏi giỏ</button>',
                '</article>'
            ].join("");
        }).join("") : '<div class="empty-state">Giỏ mượn đang trống. Hãy tra cứu sách và thêm đầu sách bạn muốn mượn.</div>';
        document.getElementById("cartBorrowButton").disabled = !items.length;
        container.querySelectorAll("[data-remove-cart]").forEach(function (button) {
            button.addEventListener("click", async function () {
                const item = items[Number(button.dataset.removeCart)];
                await submitJson("DELETE", "/api/reader/cart", { idCart: item.idCart, idBook: item.idBook }, "Đã bỏ sách khỏi giỏ.");
                renderCart();
            });
        });
    } catch (err) {
        renderError(err);
    }
}

export async function renderMyLoans() {
    setTitle("Phiếu mượn", "Không gian độc giả");
    els.pageRoot.innerHTML = [
        '<section class="surface">',
        '<div class="section-header"><div class="section-title"><h2>Phiếu mượn của tôi</h2><p>Theo dõi yêu cầu, hạn trả và lịch sử mượn sách.</p></div><button class="button button-primary" id="requestLoanButton" type="button">Tạo yêu cầu</button></div>',
        '<div class="toolbar">', fieldInline("loanSearch", "Tìm phiếu", "text"), '<button class="button button-secondary" id="loanSearchButton" type="button">Tìm</button></div>',
        '<div class="table-wrap" id="loanTable"><div class="empty-state">Đang tải phiếu mượn...</div></div>',
        '</section>'
    ].join("");
    document.getElementById("requestLoanButton").addEventListener("click", function () { openBorrowForm(false, renderMyLoans); });
    document.getElementById("loanSearchButton").addEventListener("click", loadMyLoans);
    document.getElementById("loanSearch").addEventListener("keydown", function (event) {
        if (event.key === "Enter") loadMyLoans();
    });
    await loadMyLoans();
}

async function loadMyLoans() {
    try {
        const data = await api("/api/user/borrow-ticket/idReader=" + enc(state.session.idUser));
        let rows = normalizeRows(data);
        const keyword = (document.getElementById("loanSearch") || { value: "" }).value.trim().toLowerCase();
        if (keyword) {
            rows = rows.filter(function (row) {
                return [row.idTicket, row.borrowingDate, row.dueDate, row.status].join(" ").toLowerCase().indexOf(keyword) >= 0;
            });
        }
        const table = document.getElementById("loanTable");
        table.innerHTML = tableHtml(rows, [
            ["idTicket", "Mã phiếu"], ["borrowingDate", "Ngày mượn"], ["dueDate", "Hạn trả"], ["quantity", "Số lượng"], ["status", "Trạng thái"]
        ], "Không có phiếu mượn.");
        bindTableRows(table, rows, openUserLoanDetail);
    } catch (err) {
        document.getElementById("loanTable").innerHTML = errorHtml(err);
    }
}

async function openUserLoanDetail(ticket) {
    openDrawer("Phiếu mượn", ticket.idTicket, '<div class="empty-state">Đang tải chi tiết phiếu mượn...</div>');
    try {
        const detail = unwrapData(await api("/api/borrow-ticket/idTicket=" + enc(ticket.idTicket)));
        renderUserLoanDetail(detail);
    } catch (err) {
        openDrawer("Phiếu mượn", ticket.idTicket, errorHtml(err));
    }
}

function renderUserLoanDetail(ticket) {
    openDrawer("Phiếu mượn", ticket.idTicket, [
        '<div class="page-stack">',
        detailGrid([
            ["Mã phiếu", ticket.idTicket], ["Ngày mượn", ticket.borrowingDate], ["Hạn trả", ticket.dueDate],
            ["Số lượng", ticket.quantity], ["Tổng phạt", ticket.totalFine], ["Trạng thái", ticket.status]
        ]),
        detailItems(ticket.borrowDetailTicketDTOS || []),
        '</div>'
    ].join(""));
}

function readerLoanPanel(rows) {
    return '<section class="surface"><div class="section-header"><div class="section-title"><h2>Phiếu gần đây</h2><p>Các yêu cầu mượn mới nhất của bạn.</p></div></div><div class="table-wrap">' +
        tableHtml(rows, [["idTicket", "Mã"], ["dueDate", "Hạn trả"], ["status", "Trạng thái"]], "Chưa có phiếu mượn.") + '</div></section>';
}

function readerSuggestionPanel(rows) {
    return '<section class="surface"><div class="section-header"><div class="section-title"><h2>Gợi ý đầu sách</h2><p>Một vài đầu sách mới trong kho.</p></div></div><div class="reader-suggestions">' +
        (rows.length ? rows.map(function (book) {
            return '<article class="suggestion-item"><strong>' + esc(book.nameBook || "Sách") + '</strong><span>' + esc(get(book, "authorDTO.fullName") || get(book, "categoryDTO.nameCategory") || "Đang cập nhật") + '</span></article>';
        }).join("") : '<div class="empty-state">Chưa có dữ liệu sách.</div>') +
        '</div></section>';
}

function collectCartItems(carts) {
    return carts.flatMap(function (cart) {
        return (cart.cartDetailDTOS || []).map(function (item) {
            return Object.assign({ idCart: cart.idCart }, item);
        });
    });
}
