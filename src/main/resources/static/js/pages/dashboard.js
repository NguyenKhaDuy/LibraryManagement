import { api } from "../core/api.js";
import { els } from "../core/dom.js";
import { state } from "../core/state.js";
import { normalizeRows } from "../core/utils.js";
import { chartPanel, metric, recentTicketsPanel } from "../ui/table.js";
import { renderError, setTitle } from "../ui/view.js";
import { renderReaderDashboard } from "./user.js";

export async function renderDashboard() {
    if (state.session.role === "USER") {
        await renderReaderDashboard();
        return;
    }

    setTitle("Tổng quan", "Vận hành thư viện");
    els.pageRoot.innerHTML = '<div class="page-stack"><div class="empty-state">Đang tải dữ liệu vận hành...</div></div>';
    try {
        const year = new Date().getFullYear();
        const results = await Promise.all([
            api("/api/admin/statistic/borrow?year=" + year),
            api("/api/admin/statistic/users?year=" + year),
            api("/api/admin/statistic/books?year=" + year),
            api("/api/admin/borrow-ticket?pageNo=1")
        ]);
        const borrow = results[0];
        const users = results[1];
        const books = results[2];
        const tickets = normalizeRows(results[3]).slice(0, 6);
        els.pageRoot.innerHTML = [
            '<div class="page-stack">',
            '<section class="surface ops-hero">',
            '<div><p class="eyebrow">Hôm nay</p><h2>Bảng điều phối thư viện</h2><p>Theo dõi kho sách, lượt mượn, độc giả và các phiếu cần xử lý trong cùng một màn hình.</p></div>',
            '<div class="ops-year">' + year + '</div>',
            '</section>',
            '<div class="metrics-grid">',
            metric("Sách trong kho", books.numberOfBooks, "green"),
            metric("Đang được mượn", books.numberOfBookBorrowed, "amber"),
            metric("Lượt mượn", borrow.numberOfBorrow, "violet"),
            metric("Mượn hôm nay", borrow.numberOfBorrowInDay, "green"),
            metric("Độc giả", users.numberOfReader, "amber"),
            metric("Nhân viên", users.numberOfStaff, "violet"),
            '</div>',
            '<div class="grid-2">',
            chartPanel("Lượt mượn theo tháng", borrow.statisticMonths),
            recentTicketsPanel(tickets),
            '</div>',
            '</div>'
        ].join("");
    } catch (err) {
        renderError(err);
    }
}
