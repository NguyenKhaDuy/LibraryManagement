import { els } from "../core/dom.js";
import { errorText, esc } from "../core/utils.js";
import { toast } from "./feedback.js";

export function setTitle(title, eyebrow) {
    els.pageTitle.textContent = title;
    els.pageEyebrow.textContent = eyebrow || "Kho Tri Thức";
}

export function renderError(err) {
    els.pageRoot.innerHTML = errorHtml(err);
    toast(errorText(err), "error");
}

export function errorHtml(err) {
    return '<div class="empty-state"><div><h2>Không tải được dữ liệu</h2><p>' + esc(errorText(err)) + '</p></div></div>';
}

export function loadingHtml(message) {
    return '<div class="page-stack"><div class="empty-state">' + esc(message || "Đang tải dữ liệu...") + '</div></div>';
}
