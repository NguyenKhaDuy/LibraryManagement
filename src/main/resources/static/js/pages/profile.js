import { api, submitJson } from "../core/api.js";
import { els } from "../core/dom.js";
import { state } from "../core/state.js";
import { enc, esc, escAttr, unwrapData } from "../core/utils.js";
import { staffUpdateFields, userUpdateFields } from "../config/fields.js";
import { formHtml, formValues } from "../ui/forms.js";
import { toast } from "../ui/feedback.js";
import { renderShell } from "../ui/shell.js";
import { renderError, setTitle } from "../ui/view.js";

function avatarPreviewHtml(avatar, label) {
    if (avatar) {
        return '<div class="avatar avatar-preview" id="avatarPreview"><img src="data:image/jpeg;base64,' + escAttr(avatar) + '" alt=""></div>';
    }
    return '<div class="avatar avatar-preview" id="avatarPreview">' + esc(String(label || "KT").slice(0, 2).toUpperCase()) + "</div>";
}

export async function renderProfile() {
    const isUser = state.session.role === "USER";
    setTitle("Hồ sơ", isUser ? "Độc giả" : "Nhân viên");
    els.pageRoot.innerHTML = '<div class="page-stack"><div class="empty-state">Đang tải hồ sơ...</div></div>';
    try {
        const profile = unwrapData(await api((isUser ? "/api/reader/idReader=" : "/api/staff/idStaff=") + enc(state.session.idUser)));
        const fields = isUser
            ? userUpdateFields.filter(function (f) {
                return f.name !== "status";
            })
            : staffUpdateFields;
        els.pageRoot.innerHTML = [
            '<div class="grid-2">',
            '<section class="surface"><div class="section-header"><div class="section-title"><h2>Thông tin cá nhân</h2><p>Cập nhật dữ liệu liên hệ và trạng thái tài khoản.</p></div></div><div class="drawer-body">',
            formHtml("profileForm", fields, profile, "Lưu hồ sơ"),
            '</div></section>',
            '<section class="surface"><div class="section-header"><div class="section-title"><h2>Ảnh đại diện</h2><p>Ảnh dùng trong hồ sơ và phiếu mượn.</p></div></div><div class="drawer-body"><form id="avatarForm" class="page-stack">',
            avatarPreviewHtml(profile.avatar, profile.fullName || state.session.username),
            '<label class="field"><span>Chọn ảnh</span><input name="avatar" type="file" accept="image/*" required></label>',
            '<button class="button button-primary" type="submit">Cập nhật ảnh</button></form></div></section>',
            '</div>'
        ].join("");
        document.getElementById("profileForm").addEventListener("submit", async function (event) {
            event.preventDefault();
            const payload = formValues(event.currentTarget, fields);
            await submitJson("PUT", isUser ? "/api/reader" : "/api/staff", payload, "Đã cập nhật hồ sơ.");
        });
        const avatarInput = document.querySelector('#avatarForm input[name="avatar"]');
        avatarInput.addEventListener("change", function () {
            const file = avatarInput.files && avatarInput.files[0];
            if (!file) return;
            const reader = new FileReader();
            reader.onload = function () {
                document.getElementById("avatarPreview").innerHTML = '<img src="' + reader.result + '" alt="">';
            };
            reader.readAsDataURL(file);
        });
        document.getElementById("avatarForm").addEventListener("submit", async function (event) {
            event.preventDefault();
            const file = event.currentTarget.avatar.files[0];
            const formData = new FormData();
            formData.append("idUser", state.session.idUser);
            formData.append("avatar", file);
            const result = await api(isUser ? "/api/reader/avatar" : "/api/staff/avatar", { method: "PUT", body: formData });
            const reader = new FileReader();
            reader.onload = function () {
                state.session.avatar = reader.result.split(",")[1] || "";
                renderShell(function () {
                    window.dispatchEvent(new CustomEvent("ktt:navigate"));
                });
            };
            reader.readAsDataURL(file);
            toast(result.message || "Đã cập nhật ảnh.", "success");
        });
    } catch (err) {
        renderError(err);
    }
}
