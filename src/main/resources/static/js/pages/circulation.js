import { api, optionalApi, submitJson } from "../core/api.js";
import { STATUS_VALUES } from "../core/constants.js";
import { els } from "../core/dom.js";
import { state } from "../core/state.js";
import { enc, esc, get, normalizeRows, unwrapData, valueOf } from "../core/utils.js";
import { field } from "../config/fields.js";
import { collectLines, fieldInline, fieldsHtml, formValues, initLineEditor, lineEditorHtml } from "../ui/forms.js";
import { openDrawer, closeDrawer } from "../ui/drawer.js";
import { bindTableRows, detailGrid, detailItems, tableHtml } from "../ui/table.js";
import { errorHtml, setTitle } from "../ui/view.js";

export async function renderCirculation() {
    setTitle("Mượn trả", "Vận hành thư viện");
    els.pageRoot.innerHTML = [
        '<section class="surface">',
        '<div class="section-header"><div class="section-title"><h2>Phiếu mượn</h2><p>Duyệt yêu cầu, cập nhật trạng thái và ghi nhận trả sách.</p></div><button class="button button-primary" id="newTicketButton" type="button">Tạo phiếu</button></div>',
        '<div class="toolbar">', fieldInline("ticketSearch", "Tìm phiếu", "text"), '<button class="button button-secondary" id="ticketSearchButton" type="button">Tìm</button></div>',
        '<div class="table-wrap" id="ticketTable"><div class="empty-state">Đang tải phiếu mượn...</div></div>',
        '<div class="section-header"><div class="pill-row" id="ticketPager"></div><div class="button-row"><button class="button button-secondary" id="ticketPrev" type="button">Trang trước</button><button class="button button-secondary" id="ticketNext" type="button">Trang sau</button></div></div>',
        '</section>'
    ].join("");
    document.getElementById("newTicketButton").addEventListener("click", function () { openBorrowForm(true, loadTickets); });
    document.getElementById("ticketSearchButton").addEventListener("click", loadTickets);
    document.getElementById("ticketPrev").addEventListener("click", function () {
        state.pageNo.tickets = Math.max(1, (state.pageNo.tickets || 1) - 1);
        loadTickets();
    });
    document.getElementById("ticketNext").addEventListener("click", function () {
        state.pageNo.tickets = (state.pageNo.tickets || 1) + 1;
        loadTickets();
    });
    await loadTickets();
}

async function loadTickets() {
    const pageNo = state.pageNo.tickets || 1;
    try {
        const data = await api("/api/admin/borrow-ticket?pageNo=" + pageNo);
        let rows = normalizeRows(data);
        const keyword = valueOf("ticketSearch").toLowerCase();
        if (keyword) {
            rows = rows.filter(function (row) {
                return [row.idTicket, get(row, "reader.fullName"), get(row, "staff.fullName"), row.status].join(" ").toLowerCase().indexOf(keyword) >= 0;
            });
        }
        document.getElementById("ticketTable").innerHTML = tableHtml(rows, [
            ["idTicket", "Mã phiếu"], ["reader.fullName", "Độc giả"], ["staff.fullName", "Nhân viên"], ["quantity", "SL"], ["totalFine", "Phạt"], ["status", "Trạng thái"]
        ], "Không có phiếu mượn.");
        bindTableRows(document.getElementById("ticketTable"), rows, openTicketDetail);
        document.getElementById("ticketPager").innerHTML = '<span class="pill">Trang ' + esc(data.current_page || pageNo) + ' / ' + esc(data.total_pages || 1) + '</span>';
        document.getElementById("ticketPrev").disabled = pageNo <= 1;
        document.getElementById("ticketNext").disabled = data.total_pages ? pageNo >= data.total_pages : rows.length === 0;
    } catch (err) {
        document.getElementById("ticketTable").innerHTML = errorHtml(err);
    }
}

async function openTicketDetail(ticket) {
    openDrawer("Phiếu mượn", ticket.idTicket, '<div class="empty-state">Đang tải chi tiết phiếu mượn...</div>');
    try {
        const detail = unwrapData(await api("/api/borrow-ticket/idTicket=" + enc(ticket.idTicket)));
        renderTicketDetail(detail);
    } catch (err) {
        openDrawer("Phiếu mượn", ticket.idTicket, errorHtml(err));
    }
}

function renderTicketDetail(ticket) {
    openDrawer("Phiếu mượn", ticket.idTicket, [
        '<div class="page-stack">',
        detailGrid([
            ["Mã phiếu", ticket.idTicket], ["Độc giả", get(ticket, "reader.fullName")], ["Nhân viên", get(ticket, "staff.fullName")],
            ["Ngày mượn", ticket.borrowingDate], ["Hạn trả", ticket.dueDate], ["Trạng thái", ticket.status], ["Tổng phạt", ticket.totalFine]
        ]),
        '<div class="button-row"><button class="button button-primary" id="editTicketButton" type="button">Sửa phiếu</button><button class="button button-secondary" data-status="APPROVED" type="button">Duyệt</button><button class="button button-secondary" data-status="REJECTED" type="button">Từ chối</button><button class="button button-secondary" data-status="COMPLETED" type="button">Hoàn tất</button><button class="button button-danger" id="deleteTicketButton" type="button">Xóa phiếu</button></div>',
        detailItems(ticket.borrowDetailTicketDTOS || []),
        '</div>'
    ].join(""), function () {
        document.getElementById("editTicketButton").addEventListener("click", function () {
            openBorrowForm(true, loadTickets, ticket);
        });
        document.querySelectorAll("[data-status]").forEach(function (button) {
            button.addEventListener("click", async function () {
                await submitJson("PUT", "/api/staff/borrow-ticket", { staffId: state.session.idUser, ticketId: ticket.idTicket, status: button.dataset.status }, "Đã cập nhật trạng thái.");
                closeDrawer();
                loadTickets();
            });
        });
        document.getElementById("deleteTicketButton").addEventListener("click", async function () {
            if (!confirm("Xóa phiếu mượn này?")) return;
            await submitJson("DELETE", "/api/borrow-ticket/idTicket=" + enc(ticket.idTicket), null, "Đã xóa phiếu.");
            closeDrawer();
            loadTickets();
        });
    });
}

export async function openBorrowForm(staffMode, afterSave, ticket, presetLines) {
    const isEdit = Boolean(ticket);
    const lines = Array.isArray(presetLines) ? presetLines : [];
    let cardLibraryId = "";
    if (!isEdit && !staffMode && state.session?.idUser) {
        try {
            const profile = unwrapData(await optionalApi("/api/reader/idReader=" + enc(state.session.idUser), {})) || {};
            const cards = profile.libraryCardDTOS || [];
            const activeCard = cards.find(function (card) { return card.status === "ACTIVE"; }) || cards[0];
            cardLibraryId = activeCard?.idCard || "";
        } catch (err) {
            cardLibraryId = "";
        }
    }
    const fields = [
        isEdit ? field("idTicket", "Mã phiếu", "text", { required: true, readonly: true }) : null,
        field("borrowingDate", "Ngày mượn", "datetime"),
        field("dueDate", "Hạn trả", "datetime"),
        field("quantity", "Số lượng", "number", { value: lines.length || "" }),
        field("totalFine", "Tổng phạt", "number"),
        isEdit ? field("status", "Trạng thái", "select", { options: STATUS_VALUES, required: true }) : null,
        !isEdit ? field("readerId", "Mã độc giả", "text", { required: true, value: staffMode ? "" : state.session.idUser, readonly: !staffMode }) : null,
        !isEdit ? field("cardLibraryId", "Mã thẻ thư viện", "text", { value: cardLibraryId, readonly: !staffMode && Boolean(cardLibraryId) }) : null,
        !isEdit && staffMode ? field("staffId", "Mã nhân viên", "text", { value: state.session.idUser, required: true }) : null
    ].filter(Boolean);
    const values = isEdit ? ticketFormValues(ticket) : {
        readerId: staffMode ? "" : state.session.idUser,
        staffId: staffMode ? state.session.idUser : "",
        cardLibraryId: cardLibraryId,
        quantity: lines.length || ""
    };
    openDrawer(isEdit ? "Sửa phiếu mượn" : (staffMode ? "Tạo phiếu mượn" : "Tạo yêu cầu mượn"), "Mượn trả", [
        '<form id="borrowForm" class="page-stack">',
        '<div class="form-grid">' + fieldsHtml(fields, values) + '</div>',
        lineEditorHtml("borrowLines", ["bookId:Mã sách", "statusBorrow:Trạng thái mượn", "statusReturn:Trạng thái trả", "fine:Phạt", "note:Ghi chú"]),
        '<div class="button-row"><button class="button button-primary" type="submit">Lưu phiếu</button></div>',
        '</form>'
    ].join(""), function () {
        initLineEditor("borrowLines", { statusBorrow: "BORROWING", fine: 0 }, isEdit ? ticketLineValues(ticket) : lines);
        document.getElementById("borrowForm").addEventListener("submit", async function (event) {
            event.preventDefault();
            const payload = formValues(event.currentTarget, fields);
            payload.borrowTicketDetailRequests = collectLines("borrowLines", ["bookId", "statusBorrow", "statusReturn", "fine", "note"]);
            await submitJson(isEdit ? "PUT" : "POST", isEdit ? "/api/borrow-ticket" : (staffMode ? "/api/borrow-ticket" : "/api/reader/borrow-ticket"), payload, "Đã lưu phiếu mượn.");
            closeDrawer();
            if (afterSave) afterSave();
        });
    });
}

function ticketFormValues(ticket) {
    return {
        idTicket: ticket.idTicket,
        borrowingDate: ticket.borrowingDate,
        dueDate: ticket.dueDate,
        quantity: ticket.quantity,
        totalFine: ticket.totalFine,
        status: ticket.status
    };
}

function ticketLineValues(ticket) {
    return (ticket.borrowDetailTicketDTOS || []).map(function (detail) {
        return {
            bookId: get(detail, "booksDTO.idBook"),
            statusBorrow: detail.statusBorrow,
            statusReturn: detail.statusReturn,
            fine: detail.fine,
            note: detail.note
        };
    });
}
