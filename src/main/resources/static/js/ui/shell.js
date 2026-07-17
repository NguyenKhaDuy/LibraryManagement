import { els } from "../core/dom.js";
import { state } from "../core/state.js";
import { esc, renderAvatar, roleLabel } from "../core/utils.js";
import { navByRole } from "../config/navigation.js";

let navigateHandler = null;

export function renderShell(onNavigate) {
    if (onNavigate) navigateHandler = onNavigate;
    const session = state.session;
    const navItems = navByRole[session.role] || navByRole.STAFF;

    els.productShell.dataset.role = session.role.toLowerCase();
    els.roleLabel.textContent = roleLabel(session.role);
    els.accountName.textContent = session.username || session.idUser;
    els.accountMeta.textContent = session.email || session.idUser;
    renderAvatar(els.accountAvatar, session.avatar, session.username || session.role);

    els.mainNav.innerHTML = navItems.map(function (item) {
        if (item.type === "group") {
            return '<div class="nav-group">' + esc(item.label) + '</div>';
        }
        return '<button class="nav-item ' + (state.page === item.key ? "is-active" : "") + '" type="button" data-page="' + item.key + '">' +
            '<span class="nav-icon" aria-hidden="true">' + esc(item.icon) + '</span><span>' + esc(item.label) + '</span></button>';
    }).join("");

    els.mainNav.querySelectorAll("[data-page]").forEach(function (button) {
        button.addEventListener("click", function () {
            state.page = button.dataset.page;
            renderShell();
            if (navigateHandler) navigateHandler(false);
        });
    });
}
