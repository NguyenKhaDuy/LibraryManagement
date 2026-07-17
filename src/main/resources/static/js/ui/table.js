import { get, esc, isKnownStatus, labelize, primaryLabel, statusClass, statusText } from "../core/utils.js";

export function tableHtml(rows, columns, emptyMessage) {
    if (!rows.length) {
        return '<div class="empty-state">' + esc(emptyMessage || "Không có dữ liệu.") + '</div>';
    }
    return '<table><thead><tr>' + columns.map(function (col) { return '<th>' + esc(col[1]) + '</th>'; }).join("") + '</tr></thead><tbody>' +
        rows.map(function (row, index) {
            return '<tr data-row="' + index + '">' + columns.map(function (col) {
                return '<td>' + cell(get(row, col[0])) + '</td>';
            }).join("") + '</tr>';
        }).join("") + '</tbody></table>';
}

export function bindTableRows(scope, rows, handler) {
    scope.querySelectorAll("[data-row]").forEach(function (tr) {
        tr.addEventListener("click", function () {
            handler(rows[Number(tr.dataset.row)]);
        });
    });
}

export function cell(value) {
    if (value === null || value === undefined || value === "") return '<span class="muted">-</span>';
    if (Array.isArray(value)) return '<span class="pill">' + value.length + ' mục</span>';
    if (typeof value === "object") return esc(primaryLabel(value));
    const text = String(value);
    if (isKnownStatus(text)) return '<span class="' + statusClass(text) + '">' + esc(statusText(text)) + '</span>';
    return esc(text.length > 90 ? text.slice(0, 87) + "..." : text);
}

export function metric(label, value, tone) {
    return '<article class="surface metric ' + (tone ? "metric-" + tone : "") + '"><strong>' + esc(value || 0) + '</strong><span>' + esc(label) + '</span></article>';
}

export function chartPanel(title, months) {
    const rows = Array.isArray(months) ? months : [];
    const max = rows.reduce(function (m, item) {
        return Math.max(m, Number(item.total) || 0);
    }, 0) || 1;

    return '<section class="surface">' +
        '<div class="section-header">' +
        '<div class="section-title"><h2>' + esc(title) + '</h2></div>' +
        '</div>' +

        '<div class="column-chart">' +

        (rows.length
            ? rows.map(function (item) {

                const total = Number(item.total) || 0;
                const height = Math.max(8, Math.round(total / max * 220));

                return '<div class="column-item">' +
                    '<span class="column-value">' + total + '</span>' +
                    '<div class="column-bar">' +
                    '<div class="column-fill" style="height:' + height + 'px"></div>' +
                    '</div>' +
                    '<span class="column-label">T' + esc(item.month) + '</span>' +
                    '</div>';

            }).join("")
            : '<div class="empty-state">Chưa có dữ liệu tháng.</div>')

        + '</div></section>';
}

export function recentTicketsPanel(rows) {
    return '<section class="surface"><div class="section-header"><div class="section-title"><h2>Phiếu gần đây</h2></div></div><div class="table-wrap">' +
        tableHtml(rows, [["idTicket", "Mã"], ["reader.fullName", "Độc giả"], ["status", "Trạng thái"]], "Chưa có phiếu.") + '</div></section>';
}

export function profilePanel(profile) {
    return '<section class="surface"><div class="section-header"><div class="section-title"><h2>Hồ sơ</h2></div></div><div class="drawer-body">' +
        detailGrid([["Họ tên", profile.fullName], ["Điện thoại", profile.phone], ["Email", profile.email], ["Trạng thái", profile.status]]) + '</div></section>';
}

export function loanPreviewPanel(rows) {
    return '<section class="surface"><div class="section-header"><div class="section-title"><h2>Phiếu mượn gần đây</h2></div></div><div class="table-wrap">' +
        tableHtml(rows, [["idTicket", "Mã"], ["dueDate", "Hạn trả"], ["status", "Trạng thái"]], "Chưa có phiếu mượn.") + '</div></section>';
}

export function detailGrid(items) {
    return '<div class="detail-grid">' + items.map(function (item) {
        return '<div class="detail-item"><span>' + esc(item[0]) + '</span><strong>' + cell(item[1]) + '</strong></div>';
    }).join("") + '</div>';
}

export function detailObject(obj) {
    const items = Object.keys(obj || {}).filter(function (key) {
        return obj[key] !== null && obj[key] !== undefined && typeof obj[key] !== "function";
    }).slice(0, 16).map(function (key) {
        return [labelize(key), Array.isArray(obj[key]) ? obj[key].length + " mục" : (typeof obj[key] === "object" ? primaryLabel(obj[key]) : obj[key])];
    });
    return detailGrid(items);
}

export function detailItems(items) {
    if (!items.length) return "";
    return '<div class="page-stack">' + items.map(function (item) {
        const book = item.booksDTO || item.book || {};
        return '<article class="mini-card"><h3>' + esc(book.nameBook || item.nameBook || book.idBook || item.bookId || "Sách") + '</h3><div class="meta-list"><span>' + esc(get(book, "authorDTO.fullName") || item.nameAuthor || "") + '</span><span>' + esc(item.note || item.statusBorrow || "") + '</span></div></article>';
    }).join("") + '</div>';
}
