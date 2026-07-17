import { enc } from "../core/utils.js";
import {
    authorFields,
    categoryFields, getStaffUpdateFields,
    libraryCardFields,
    publisherFields,
    registerFields,
    shelfFields,
    staffCreateFields,
    staffUpdateFields,
    supplierFields,
    userUpdateFields
} from "./fields.js";

export const resourceConfigs = {
    readers: {
        title: "Độc giả",
        eyebrow: "Quản lý người dùng",
        list: "/api/admin/reader",
        detail: function (row) { return "/api/reader/idReader=" + enc(row.idUser || row.idReader); },
        create: { label: "Thêm độc giả", method: "POST", path: "/api/register", fields: registerFields },
        update: { method: "PUT", path: "/api/reader", fields: userUpdateFields },
        remove: function (row) { return "/api/reader/idReader=" + enc(row.idUser || row.idReader); },
        columns: [
            ["idUser", "Mã"], ["fullName", "Họ tên"], ["phone", "Điện thoại"], ["email", "Email"], ["status", "Trạng thái"]
        ],
        search: ["idUser", "fullName", "phone", "email", "cccd"]
    },
    staff: {
        title: "Nhân viên",
        eyebrow: "Tổ chức",
        list: "/api/admin/staff",
        detail: function (row) { return "/api/staff/idStaff=" + enc(row.idUser || row.idStaff); },
        create: { label: "Thêm nhân viên", method: "POST", path: "/api/admin/register/staff", fields: staffCreateFields },
        update: { method: "PUT", path: "/api/staff", fields: getStaffUpdateFields },
        remove: function (row) { return "/api/staff/idStaff=" + enc(row.idUser || row.idStaff); },
        columns: [
            ["idUser", "Mã"], ["fullName", "Họ tên"], ["position", "Vị trí"], ["phone", "Điện thoại"], ["status", "Trạng thái"]
        ],
        search: ["idUser", "fullName", "phone", "email", "position"]
    },
    libraryCards: {
        title: "Thẻ thư viện",
        eyebrow: "Độc giả",
        list: "/api/admin/library-card",
        detail: function (row) { return "/api/library-card/idCard=" + enc(row.idCard); },
        create: { label: "Cấp thẻ", method: "POST", path: "/api/staff/library-card", fields: libraryCardFields(false) },
        update: { method: "PUT", path: "/api/staff/library-card", fields: libraryCardFields(true) },
        remove: function (row) { return "/api/staff/library-card/idCard=" + enc(row.idCard); },
        columns: [
            ["idCard", "Mã thẻ"], ["reader.fullName", "Độc giả"], ["dateOfIssue", "Ngày cấp"], ["expirationDate", "Hết hạn"], ["status", "Trạng thái"]
        ],
        search: ["idCard", "reader.fullName", "reader.idReader"]
    },
    suppliers: {
        title: "Nhà cung cấp",
        eyebrow: "Kho sách",
        list: "/api/admin/supplier",
        detail: function (row) { return "/api/admin/supplier/idSupplier=" + enc(row.idSupplier); },
        create: { label: "Thêm nhà cung cấp", method: "POST", path: "/api/admin/supplier", fields: supplierFields(false) },
        update: { method: "PUT", path: "/api/admin/supplier", fields: supplierFields(true) },
        remove: function (row) { return "/api/admin/supplier/idSupplier=" + enc(row.idSupplier); },
        columns: [
            ["idSupplier", "Mã"], ["nameSupplier", "Tên"], ["phone", "Điện thoại"], ["email", "Email"], ["address", "Địa chỉ"]
        ],
        search: ["idSupplier", "nameSupplier", "phone", "email"]
    },
    categories: {
        title: "Thể loại",
        eyebrow: "Thiết lập danh mục",
        list: "/api/category",
        detail: function (row) { return "/api/category/id-category=" + enc(row.idCategory); },
        create: { label: "Thêm thể loại", method: "POST", path: "/api/admin/category", fields: categoryFields(false) },
        update: { method: "PUT", path: "/api/admin/category", fields: categoryFields(true) },
        remove: function (row) { return "/api/admin/category/id-category=" + enc(row.idCategory); },
        columns: [["idCategory", "Mã"], ["nameCategory", "Tên"], ["description", "Mô tả"], ["createdAt", "Ngày tạo"]],
        search: ["idCategory", "nameCategory", "description"]
    },
    authors: {
        title: "Tác giả",
        eyebrow: "Thiết lập danh mục",
        list: "/api/admin/author",
        detail: function (row) { return "/api/admin/author/id-author=" + enc(row.idAuthor); },
        create: { label: "Thêm tác giả", method: "POST", path: "/api/admin/author", fields: authorFields(false) },
        update: { method: "PUT", path: "/api/admin/author", fields: authorFields(true) },
        remove: function (row) { return "/api/admin/author/id-author=" + enc(row.idAuthor); },
        columns: [["idAuthor", "Mã"], ["fullName", "Họ tên"], ["dob", "Ngày sinh"], ["homeTown", "Quê quán"], ["nationality", "Quốc tịch"]],
        search: ["idAuthor", "fullName", "homeTown", "nationality"]
    },
    shelves: {
        title: "Kệ sách",
        eyebrow: "Thiết lập danh mục",
        list: "/api/admin/book-shelf",
        detail: function (row) { return "/api/admin/book-shelf/idBookShelf=" + enc(row.idBookshelf); },
        create: { label: "Thêm kệ", method: "POST", path: "/api/admin/book-shelf", fields: shelfFields(false) },
        update: { method: "PUT", path: "/api/admin/book-shelf", fields: shelfFields(true) },
        remove: function (row) { return "/api/admin/book-shelf/idBookShelf=" + enc(row.idBookshelf); },
        columns: [["idBookshelf", "Mã"], ["location", "Vị trí"], ["floor", "Tầng"], ["area", "Khu"], ["capacity", "Sức chứa"], ["currentCapacity", "Còn lại"]],
        search: ["idBookshelf", "location", "area"]
    },
    publishers: {
        title: "Nhà xuất bản",
        eyebrow: "Thiết lập danh mục",
        list: "/api/admin/publishing-house",
        detail: function (row) { return "/api/admin/publishing-house/id=" + enc(row.idPublishingHouse); },
        create: { label: "Thêm NXB", method: "POST", path: "/api/admin/publishing-house", fields: publisherFields(false) },
        update: { method: "PUT", path: "/api/admin/publishing-house", fields: publisherFields(true) },
        remove: function (row) { return "/api/admin/publishing-house/id=" + enc(row.idPublishingHouse); },
        columns: [["idPublishingHouse", "Mã"], ["name", "Tên"], ["phone", "Điện thoại"], ["email", "Email"], ["website", "Website"]],
        search: ["idPublishingHouse", "name", "phone", "email"]
    }
};
