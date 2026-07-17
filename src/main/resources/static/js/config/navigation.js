function nav(key, label, icon) {
    return { type: "item", key: key, label: label, icon: icon };
}

function group(label) {
    return { type: "group", label: label };
}

export const navByRole = {
    USER: [
        group("Không gian độc giả"),
        nav("dashboard", "Tổng quan", "⌂"),
        nav("catalog", "Tra cứu sách", "⌕"),
        nav("cart", "Giỏ mượn", "▣"),
        nav("myLoans", "Phiếu mượn", "✓"),
        nav("profile", "Hồ sơ", "◇")
    ],
    STAFF: [
        group("Vận hành"),
        nav("dashboard", "Tổng quan", "⌂"),
        // nav("books", "Kho sách", "B"),
        nav("circulation", "Mượn trả", "T"),
        nav("readers", "Độc giả", "R"),
        nav("libraryCards", "Thẻ thư viện", "C"),
        // group("Quản trị"),
        // nav("staff", "Nhân viên", "S"),
        nav("imports", "Nhập sách", "I"),
        // nav("suppliers", "Nhà cung cấp", "N"),
        // nav("setup", "Danh mục", "D"),
        nav("profile", "Hồ sơ", "◇")
    ],
    ADMIN: [
        group("Quản trị"),
        nav("dashboard", "Tổng quan", "⌂"),
        nav("books", "Kho sách", "B"),
        nav("circulation", "Mượn trả", "T"),
        nav("readers", "Độc giả", "R"),
        nav("staff", "Nhân viên", "S"),
        nav("libraryCards", "Thẻ thư viện", "C"),
        nav("imports", "Nhập sách", "I"),
        nav("suppliers", "Nhà cung cấp", "N"),
        nav("setup", "Danh mục", "D"),
        nav("profile", "Hồ sơ", "◇")
    ]
};

export function allowedPagesForRole(role) {
    return (navByRole[role] || navByRole.STAFF)
        .filter(function (item) { return item.type === "item"; })
        .map(function (item) { return item.key; });
}

export function firstPageForRole(role) {
    return allowedPagesForRole(role)[0] || "dashboard";
}
