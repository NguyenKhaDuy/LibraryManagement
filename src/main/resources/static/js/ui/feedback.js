import { els } from "../core/dom.js";

export function toast(message, type) {
    const node = document.createElement("div");
    node.className = "toast" + (type ? " is-" + type : "");
    node.textContent = message;
    els.toastStack.appendChild(node);
    window.setTimeout(function () { node.remove(); }, 4300);
}

export function setLoading(button, loading) {
    if (!button) return;
    button.disabled = loading;
    button.classList.toggle("loading", loading);
}
