import { api, submitJson } from "../core/api.js";
import { els } from "../core/dom.js";
import { state } from "../core/state.js";
import { esc, get, normalizeRows, primaryLabel, unwrapData, valueOf } from "../core/utils.js";
import { resourceConfigs } from "../config/resources.js";
import { fieldInline, formHtml, formValues } from "../ui/forms.js";
import { openDrawer, closeDrawer } from "../ui/drawer.js";
import { bindTableRows, detailObject, tableHtml } from "../ui/table.js";
import { errorHtml, setTitle } from "../ui/view.js";
import { toast } from "../ui/feedback.js";
import { errorText } from "../core/utils.js";

export { resourceConfigs };

export async function renderResource(config) {
    setTitle(config.title, config.eyebrow);
    els.pageRoot.innerHTML = [
        '<section class="surface">',
        '<div class="section-header"><div class="section-title"><h2>' + esc(config.title) + '</h2><p>Quản lý và cập nhật dữ liệu vận hành.</p></div><button class="button button-primary" id="resourceCreateButton" type="button">' + esc(config.create.label) + '</button></div>',
        '<div class="toolbar">' + fieldInline("resourceSearch", "Tìm kiếm", "text") + '<button class="button button-secondary" id="resourceReloadButton" type="button">Làm mới</button></div>',
        '<div class="table-wrap" id="resourceTable"><div class="empty-state">Đang tải dữ liệu...</div></div>',
        '<div class="section-header"><div class="pill-row" id="resourcePager"></div><div class="button-row"><button class="button button-secondary" id="resourcePrev" type="button">Trang trước</button><button class="button button-secondary" id="resourceNext" type="button">Trang sau</button></div></div>',
        '</section>'
    ].join("");
    document.getElementById("resourceCreateButton").addEventListener("click", function () { openResourceForm(config, null); });
    document.getElementById("resourceReloadButton").addEventListener("click", function () { loadResource(config); });
    document.getElementById("resourceSearch").addEventListener("keydown", function (event) {
        if (event.key === "Enter") loadResource(config);
    });
    document.getElementById("resourcePrev").addEventListener("click", function () {
        state.pageNo[state.page] = Math.max(1, (state.pageNo[state.page] || 1) - 1);
        loadResource(config);
    });
    document.getElementById("resourceNext").addEventListener("click", function () {
        state.pageNo[state.page] = (state.pageNo[state.page] || 1) + 1;
        loadResource(config);
    });
    await loadResource(config);
}

async function loadResource(config) {
    const pageKey = state.page;
    const pageNo = state.pageNo[pageKey] || 1;
    try {
        const data = await api(config.list + (config.list.indexOf("?") >= 0 ? "&" : "?") + "pageNo=" + pageNo);
        let rows = normalizeRows(data);
        const keyword = valueOf("resourceSearch").toLowerCase();
        if (keyword) {
            rows = rows.filter(function (row) {
                return (config.search || []).map(function (path) { return get(row, path); }).join(" ").toLowerCase().indexOf(keyword) >= 0;
            });
        }
        const table = document.getElementById("resourceTable");
        table.innerHTML = tableHtml(rows, config.columns, "Không có dữ liệu.");
        bindTableRows(table, rows, function (row) { openResourceDetail(config, row); });
        document.getElementById("resourcePager").innerHTML = '<span class="pill">Trang ' + esc(data.current_page || pageNo) + ' / ' + esc(data.total_pages || 1) + '</span><span class="pill pill-warning">' + rows.length + ' bản ghi</span>';
        document.getElementById("resourcePrev").disabled = pageNo <= 1;
        document.getElementById("resourceNext").disabled = data.total_pages ? pageNo >= data.total_pages : rows.length === 0;
    } catch (err) {
        document.getElementById("resourceTable").innerHTML = errorHtml(err);
    }
}

async function openResourceDetail(config, row) {
    try {
        const detail = config.detail ? unwrapData(await api(config.detail(row))) : row;
        openDrawer(config.title, primaryLabel(detail), [
            detailObject(detail),
            '<div class="button-row"><button class="button button-primary" id="editResourceButton" type="button">Sửa</button><button class="button button-danger" id="deleteResourceButton" type="button">Xóa</button></div>'
        ].join(""), function () {
            document.getElementById("editResourceButton").addEventListener("click", function () { openResourceForm(config, detail); });
            document.getElementById("deleteResourceButton").addEventListener("click", async function () {
                if (!confirm("Xóa bản ghi này?")) return;
                await submitJson("DELETE", config.remove(detail), null, "Đã xóa bản ghi.");
                closeDrawer();
                loadResource(config);
            });
        });
    } catch (err) {
        toast(errorText(err), "error");
    }
}

function openResourceForm(config, row) {
    const isEdit = Boolean(row);
    const action = isEdit ? config.update : config.create;
    const values = row ? flattenForForm(row, action.fields) : {};
    openDrawer(isEdit ? "Cập nhật" : action.label, config.title, formHtml("resourceForm", action.fields, values, isEdit ? "Lưu thay đổi" : action.label), function () {
        document.getElementById("resourceForm").addEventListener("submit", async function (event) {
            event.preventDefault();
            const payload = formValues(event.currentTarget, action.fields);
            await submitJson(action.method, action.path, payload, "Đã lưu bản ghi.");
            closeDrawer();
            loadResource(config);
        });
    });
}

export function renderSetup() {
    setTitle("Danh mục", "Thiết lập hệ thống");
    const keys = ["categories", "authors", "shelves", "publishers"];
    els.pageRoot.innerHTML = [
        '<section class="surface">',
        '<div class="section-header"><div class="section-title"><h2>Dữ liệu nền</h2><p>Thể loại, tác giả, kệ sách và nhà xuất bản.</p></div></div>',
        '<div class="tabs">' + keys.map(function (key, index) { return '<button class="tab ' + (index === 0 ? "is-active" : "") + '" data-setup="' + key + '" type="button">' + esc(resourceConfigs[key].title) + '</button>'; }).join("") + '</div>',
        '<div id="setupMount" class="page-stack setup-mount"></div>',
        '</section>'
    ].join("");
    els.pageRoot.querySelectorAll("[data-setup]").forEach(function (button) {
        button.addEventListener("click", function () {
            els.pageRoot.querySelectorAll("[data-setup]").forEach(function (b) { b.classList.toggle("is-active", b === button); });
            renderInlineResource(resourceConfigs[button.dataset.setup]);
        });
    });
    renderInlineResource(resourceConfigs.categories);
}

async function renderInlineResource(config) {
    const mount = document.getElementById("setupMount");
    mount.innerHTML = '<div class="empty-state">Đang tải ' + esc(config.title.toLowerCase()) + '...</div>';
    try {
        const data = await api(config.list + (config.list === "/api/category" ? "" : "?pageNo=1"));
        const rows = normalizeRows(data);
        mount.innerHTML = [
            '<div class="section-header"><div class="section-title"><h2>' + esc(config.title) + '</h2></div><button class="button button-primary" id="inlineCreate" type="button">' + esc(config.create.label) + '</button></div>',
            '<div class="table-wrap">' + tableHtml(rows, config.columns, "Không có dữ liệu.") + '</div>'
        ].join("");
        document.getElementById("inlineCreate").addEventListener("click", function () { openResourceForm(config, null); });
        bindTableRows(mount, rows, function (row) { openResourceDetail(config, row); });
    } catch (err) {
        mount.innerHTML = errorHtml(err);
    }
}

function flattenForForm(row, fields) {
    const values = {};
    fields.forEach(function (f) {
        values[f.name] = row[f.name];
    });
    values.idReader = values.idReader || get(row, "reader.idReader");
    values.idUser = values.idUser || row.idUser || row.idReader || row.idStaff;
    values.idSupplier = values.idSupplier || get(row, "supplierDTO.idSupplier");
    return values;
}
