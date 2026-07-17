import { api, submitJson } from "../core/api.js";
import { els } from "../core/dom.js";
import { state } from "../core/state.js";
import { enc, esc, get, normalizeRows, unwrapData } from "../core/utils.js";
import { field } from "../config/fields.js";
import { collectLines, fieldsHtml, formValues, initLineEditor, lineEditorHtml } from "../ui/forms.js";
import { openDrawer, closeDrawer } from "../ui/drawer.js";
import { bindTableRows, detailGrid, detailItems, tableHtml } from "../ui/table.js";
import { errorHtml, setTitle } from "../ui/view.js";

export async function renderImports() {
    setTitle("Nhập sách", "Kho sách");
    els.pageRoot.innerHTML = [
        '<section class="surface">',
        '<div class="section-header"><div class="section-title"><h2>Phiếu nhập</h2><p>Ghi nhận sách nhập từ nhà cung cấp.</p></div><button class="button button-primary" id="newImportButton" type="button">Tạo phiếu nhập</button></div>',
        '<div class="table-wrap" id="importTable"><div class="empty-state">Đang tải phiếu nhập...</div></div>',
        '<div class="section-header"><div class="pill-row" id="importPager"></div><div class="button-row"><button class="button button-secondary" id="importPrev" type="button">Trang trước</button><button class="button button-secondary" id="importNext" type="button">Trang sau</button></div></div>',
        '</section>'
    ].join("");
    document.getElementById("newImportButton").addEventListener("click", function () { openImportForm(null); });
    document.getElementById("importPrev").addEventListener("click", function () {
        state.pageNo.imports = Math.max(1, (state.pageNo.imports || 1) - 1);
        loadImports();
    });
    document.getElementById("importNext").addEventListener("click", function () {
        state.pageNo.imports = (state.pageNo.imports || 1) + 1;
        loadImports();
    });
    await loadImports();
}

async function loadImports() {
    const pageNo = state.pageNo.imports || 1;
    try {
        const data = await api("/api/staff/import/book?pageNo=" + pageNo);
        const rows = normalizeRows(data);
        document.getElementById("importTable").innerHTML = tableHtml(rows, [
            ["idImport", "Mã phiếu"], ["importDate", "Ngày nhập"], ["staffName", "Nhân viên"], ["supplierDTO.nameSupplier", "Nhà cung cấp"], ["totalPrice", "Tổng tiền"]
        ], "Không có phiếu nhập.");
        bindTableRows(document.getElementById("importTable"), rows, function (row) { openImportDetail(row); });
        document.getElementById("importPager").innerHTML = '<span class="pill">Trang ' + esc(data.current_page || pageNo) + ' / ' + esc(data.total_pages || 1) + '</span>';
        document.getElementById("importPrev").disabled = pageNo <= 1;
        document.getElementById("importNext").disabled = data.total_pages ? pageNo >= data.total_pages : rows.length === 0;
    } catch (err) {
        document.getElementById("importTable").innerHTML = errorHtml(err);
    }
}

async function openImportDetail(row) {
    openDrawer("Phiếu nhập", row.idImport, '<div class="empty-state">Đang tải chi tiết phiếu nhập...</div>');
    try {
        const detail = unwrapData(await api("/api/staff/import/book/idImport=" + enc(row.idImport)));
        renderImportDetail(detail);
    } catch (err) {
        openDrawer("Phiếu nhập", row.idImport, errorHtml(err));
    }
}

function renderImportDetail(row) {
    openDrawer("Phiếu nhập", row.idImport, [
        detailGrid([
            ["Mã phiếu", row.idImport], ["Ngày nhập", row.importDate], ["Nhân viên", row.staffName],
            ["Nhà cung cấp", get(row, "supplierDTO.nameSupplier")], ["Tổng tiền", row.totalPrice], ["Ghi chú", row.note]
        ]),
        '<div class="button-row"><button class="button button-primary" id="editImportButton" type="button">Sửa phiếu</button><button class="button button-danger" id="deleteImportButton" type="button">Xóa phiếu</button></div>',
        detailItems(row.importBookDetailDTOS || [])
    ].join(""), function () {
        document.getElementById("editImportButton").addEventListener("click", function () { openImportForm(row); });
        document.getElementById("deleteImportButton").addEventListener("click", async function () {
            if (!confirm("Xóa phiếu nhập này?")) return;
            await submitJson("DELETE", "/api/staff/import/book/idImport=" + enc(row.idImport), null, "Đã xóa phiếu nhập.");
            closeDrawer();
            loadImports();
        });
    });
}

function openImportForm(row) {
    const isEdit = Boolean(row);
    const fields = [
        field("idImport", "Mã phiếu", "text", { readonly: true, hidden: !isEdit }),
        field("importDate", "Ngày nhập", "date"),
        field("totalPrice", "Tổng tiền", "number"),
        field("staffId", "Mã nhân viên", "text", { value: state.session.idUser, required: true }),
        field("supplierId", "Mã nhà cung cấp", "number", { required: true }),
        field("note", "Ghi chú", "textarea", { wide: true })
    ].filter(function (f) { return !f.hidden; });
    const values = row ? {
        idImport: row.idImport,
        importDate: row.importDate,
        totalPrice: row.totalPrice,
        staffId: row.staffId || state.session.idUser,
        supplierId: get(row, "supplierDTO.idSupplier"),
        note: row.note
    } : { staffId: state.session.idUser };
    openDrawer(isEdit ? "Sửa phiếu nhập" : "Tạo phiếu nhập", "Nhập sách", [
        '<form id="importForm" class="page-stack">',
        '<div class="form-grid">' + fieldsHtml(fields, values) + '</div>',
        lineEditorHtml("importLines", ["bookId:Mã sách", "price:Đơn giá", "quantity:Số lượng", "totalPrice:Thành tiền", "note:Ghi chú"]),
        '<div class="button-row"><button class="button button-primary" type="submit">Lưu phiếu</button></div>',
        '</form>'
    ].join(""), function () {
        initLineEditor("importLines", { quantity: 1, price: 0, totalPrice: 0 }, (row && row.importBookDetailDTOS || []).map(function (detail) {
            return {
                bookId: get(detail, "booksDTO.idBook"),
                price: detail.price,
                quantity: detail.quantity,
                totalPrice: detail.totalPrice,
                note: detail.note
            };
        }));
        document.getElementById("importForm").addEventListener("submit", async function (event) {
            event.preventDefault();
            const payload = formValues(event.currentTarget, fields);
            payload.importDetailBookRequests = collectLines("importLines", ["bookId", "price", "quantity", "totalPrice", "note"]);
            await submitJson(isEdit ? "PUT" : "POST", "/api/staff/import/book", payload, "Đã lưu phiếu nhập.");
            closeDrawer();
            loadImports();
        });
    });
}
