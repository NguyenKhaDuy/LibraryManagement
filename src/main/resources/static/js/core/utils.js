import { STATUS_VALUES } from "./constants.js";

export function get(obj, path) {
    if (!obj || !path) return undefined;
    return path.split(".").reduce(function (current, key) {
        return current && current[key] !== undefined ? current[key] : undefined;
    }, obj);
}

export function normalizeRows(data) {
    if (!data) return [];
    if (Array.isArray(data)) return data;
    if (Array.isArray(data.data)) return data.data;
    if (data.data && typeof data.data === "object") return [data.data];
    if (typeof data === "object" && !data.message && !data.status) return [data];
    return [];
}

export function unwrapData(data) {
    return data && data.data !== undefined ? data.data : data;
}

export function query(params) {
    const entries = Object.keys(params).filter(function (key) {
        return params[key] !== undefined && params[key] !== null && params[key] !== "";
    }).map(function (key) {
        return enc(key) + "=" + enc(params[key]);
    });
    return entries.length ? "?" + entries.join("&") : "";
}

export function enc(value) {
    return encodeURIComponent(value == null ? "" : String(value));
}

export function esc(value) {
    return String(value == null ? "" : value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

export function escAttr(value) {
    return esc(value).replace(/`/g, "&#096;");
}

export function valueOf(id) {
    const input = document.getElementById(id);
    return input ? input.value.trim() : "";
}

export function normalizeRole(role) {
    const value = String(role || "").toUpperCase();
    if (value === "ADMIN") return "ADMIN";
    if (value === "USER" || value === "READER") return "USER";
    return "STAFF";
}

export function roleLabel(role) {
    if (role === "USER") return "Độc giả";
    if (role === "ADMIN") return "Quản trị viên";
    return "Nhân viên thư viện";
}

export function defaultPageForPath(role) {
    if (role === "USER" || location.pathname === "/user") return "dashboard";
    if (location.pathname === "/staff" || location.pathname === "/admin") return "dashboard";
    return "dashboard";
}

export function statusClass(status) {
    if (["ACTIVE", "AVAILABLE", "APPROVED", "COMPLETED", "RETURNED"].indexOf(status) >= 0) return "pill pill-success";
    if (["PENDING", "WAITTING", "BORROWING", "RESERVED", "OVERDUE"].indexOf(status) >= 0) return "pill pill-warning";
    if (["LOCKED", "BANNED", "DELETED", "LOST", "DAMAGED", "REJECTED", "CANCELLED"].indexOf(status) >= 0) return "pill pill-danger";
    return "pill";
}

export function firstImage(book) {
    const image = book && book.imageDTOS && book.imageDTOS[0] && book.imageDTOS[0].imageBase64;
    return image ? "data:image/jpeg;base64," + image : "";
}

export function primaryLabel(row) {
    return row.fullName || row.nameBook || row.nameCategory || row.nameSupplier || row.name || row.idUser || row.idBook || row.idTicket || row.idCard || row.idImport || row.idAuthor || row.idSupplier || "Bản ghi";
}

export function labelize(key) {
    const labels = {
        idUser: "Mã người dùng",
        idReader: "Mã độc giả",
        idStaff: "Mã nhân viên",
        idBook: "Mã sách",
        idTicket: "Mã phiếu",
        idCard: "Mã thẻ",
        fullName: "Họ tên",
        dob: "Ngày sinh",
        cccd: "CCCD",
        phone: "Điện thoại",
        email: "Email",
        address: "Địa chỉ",
        status: "Trạng thái"
    };
    return labels[key] || key.replace(/([A-Z])/g, " $1").replace(/^./, function (c) { return c.toUpperCase(); });
}

export function renderAvatar(target, avatar, label) {
    if (avatar) {
        target.innerHTML = '<img src="data:image/jpeg;base64,' + escAttr(avatar) + '" alt="">';
    } else {
        target.textContent = String(label || "KT").slice(0, 2).toUpperCase();
    }
}

export function formatDateTimeInput(value) {
    return value ? String(value).slice(0, 16) : "";
}

export function errorText(err) {
    return (err && err.data && err.data.message) || (err && err.message) || "Đã có lỗi xảy ra.";
}

export function tryJson(text) {
    try {
        return JSON.parse(text);
    } catch (err) {
        return { message: text };
    }
}

export function statusText(status) {
    const labels = {
        AVAILABLE: "Có sẵn",
        BORROWED: "Đang mượn",
        RESERVED: "Đã đặt",
        RETURNED: "Đã trả",
        OVERDUE: "Quá hạn",
        LOST: "Mất",
        DAMAGED: "Hư hỏng",
        MAINTENANCE: "Bảo trì",
        REMOVED: "Đã gỡ",
        PENDING: "Chờ duyệt",
        ACTIVE: "Đang hoạt động",
        INACTIVE: "Ngừng hoạt động",
        LOCKED: "Đã khóa",
        SUSPENDED: "Tạm ngưng",
        EXPIRED: "Hết hạn",
        DEBT: "Còn nợ",
        BANNED: "Bị chặn",
        DISABLED: "Vô hiệu",
        DELETED: "Đã xóa",
        EMPTY: "Trống",
        FULL: "Đầy",
        WAITTING: "Đang chờ",
        APPROVED: "Đã duyệt",
        REJECTED: "Từ chối",
        CANCELLED: "Đã hủy",
        COMPLETED: "Hoàn tất",
        BORROWING: "Đang mượn"
    };
    return labels[status] || status;
}

export function isKnownStatus(value) {
    return STATUS_VALUES.indexOf(String(value)) >= 0;
}
