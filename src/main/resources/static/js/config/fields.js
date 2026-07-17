import { STATUS_VALUES } from "../core/constants.js";
import { get } from "../core/utils.js";

export function field(name, label, type, options) {
    return Object.assign({ name: name, label: label, type: type || "text" }, options || {});
}

export const registerFields = [
    field("userName", "Tên đăng nhập", "text", { required: true }),
    field("password", "Mật khẩu", "password", { required: true }),
    field("fullName", "Họ và tên", "text", { required: true }),
    field("dob", "Ngày sinh", "date"),
    field("gender", "Giới tính"),
    field("phone", "Số điện thoại"),
    field("email", "Email", "email"),
    field("cccd", "CCCD"),
    field("address", "Địa chỉ", "textarea", { wide: true })
];

export const userUpdateFields = [
    field("idUser", "Mã người dùng", "text", { required: true, readonly: true }),
    field("fullName", "Họ và tên", "text", { required: true }),
    field("dob", "Ngày sinh", "date"),
    field("gender", "Giới tính"),
    field("phone", "Số điện thoại"),
    field("email", "Email", "email"),
    field("cccd", "CCCD"),
    field("status", "Trạng thái", "select", { options: STATUS_VALUES }),
    field("address", "Địa chỉ", "textarea", { wide: true })
];

export const staffUpdateFields = userUpdateFields.concat([
    field("dateStart", "Ngày bắt đầu", "date"),
    field("wage", "Lương", "number"),
    field("position", "Vị trí")
]);

export const staffCreateFields = registerFields.concat([
    field("dateStart", "Ngày bắt đầu", "date"),
    field("wage", "Lương", "number"),
    field("position", "Vị trí")
]);

export const bookFields = [
    field("idBook", "Mã sách", "text", { readonly: true }),
    field("nameBook", "Tên sách", "text", { required: true }),
    field("yearOfPub", "Năm xuất bản", "number"),
    field("quantity", "Số lượng", "number"),
    field("edition", "Lần tái bản", "number"),
    field("language", "Ngôn ngữ"),
    field("valueOfBook", "Giá trị", "number"),
    field("authorId", "Mã tác giả", "text", { required: true }),
    field("categoryId", "Mã thể loại", "number", { required: true }),
    field("bookShelfId", "Mã kệ", "number", { required: true }),
    field("publisherId", "Mã nhà xuất bản", "number", { required: true }),
    field("status", "Trạng thái", "select", { options: STATUS_VALUES }),
    field("description", "Mô tả", "textarea", { wide: true }),
    field("images", "Ảnh sách", "file", { wide: true, multiple: true, accept: "image/*" })
];

export function mapBookToForm(book) {
    return {
        idBook: book.idBook,
        nameBook: book.nameBook,
        yearOfPub: book.yearOfPub,
        quantity: book.quantity,
        edition: book.edition,
        language: book.language,
        valueOfBook: book.valueOfBook,
        description: book.description,
        authorId: get(book, "authorDTO.idAuthor"),
        categoryId: get(book, "categoryDTO.idCategory"),
        bookShelfId: get(book, "bookshelfDTO.idBookshelf"),
        publisherId: get(book, "publishingHouseDTO.idPublishingHouse"),
        status: book.status
    };
}

export function libraryCardFields(includeId) {
    return [
        includeId ? field("idCard", "Mã thẻ", "text", { required: true, readonly: true }) : null,
        field("dateOfIssue", "Ngày cấp", "datetime"),
        field("expirationDate", "Ngày hết hạn", "date"),
        field("status", "Trạng thái", "select", { options: STATUS_VALUES }),
        field("idReader", "Mã độc giả", "text", { required: true })
    ].filter(Boolean);
}

export function supplierFields(includeId) {
    return [
        includeId ? field("idSupplier", "Mã NCC", "number", { required: true, readonly: true }) : null,
        field("nameSupplier", "Tên nhà cung cấp", "text", { required: true }),
        field("phone", "Điện thoại"),
        field("email", "Email", "email"),
        field("address", "Địa chỉ", "textarea", { wide: true })
    ].filter(Boolean);
}

export function categoryFields(includeId) {
    return [
        includeId ? field("idCategory", "Mã thể loại", "number", { required: true, readonly: true }) : null,
        field("nameCategory", "Tên thể loại", "text", { required: true }),
        field("description", "Mô tả", "textarea", { wide: true })
    ].filter(Boolean);
}

export function authorFields(includeId) {
    return [
        includeId ? field("idAuthor", "Mã tác giả", "text", { required: true, readonly: true }) : null,
        field("fullName", "Họ tên", "text", { required: true }),
        field("dob", "Ngày sinh", "date"),
        field("homeTown", "Quê quán"),
        field("nationality", "Quốc tịch")
    ].filter(Boolean);
}

export function shelfFields(includeId) {
    return [
        includeId ? field("idBookshelf", "Mã kệ", "number", { required: true, readonly: true }) : null,
        field("location", "Vị trí", "text", { required: true }),
        field("floor", "Tầng", "number"),
        field("area", "Khu"),
        field("capacity", "Sức chứa", "number")
    ].filter(Boolean);
}

export function publisherFields(includeId) {
    return [
        includeId ? field("idPublishingHouse", "Mã NXB", "number", { required: true, readonly: true }) : null,
        field("name", "Tên nhà xuất bản", "text", { required: true }),
        field("phone", "Điện thoại"),
        field("email", "Email", "email"),
        field("website", "Website"),
        field("address", "Địa chỉ", "textarea", { wide: true })
    ].filter(Boolean);
}
