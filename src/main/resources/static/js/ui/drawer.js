import { els } from "../core/dom.js";

export function openDrawer(title, eyebrow, html, afterOpen) {
    els.drawerTitle.textContent = title;
    els.drawerEyebrow.textContent = eyebrow || "Chi tiết";
    els.drawerBody.innerHTML = html;
    els.drawer.classList.add("is-open");
    els.drawer.setAttribute("aria-hidden", "false");
    if (afterOpen) afterOpen();
}

export function closeDrawer() {
    els.drawer.classList.remove("is-open");
    els.drawer.setAttribute("aria-hidden", "true");
}
