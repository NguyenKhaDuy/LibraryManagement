import { api, submitJson } from "../core/api.js";
import { els } from "../core/dom.js";
import { state } from "../core/state.js";
import { enc, unwrapData } from "../core/utils.js";
import { staffUpdateFields, userUpdateFields } from "../config/fields.js";
import { formHtml, formValues } from "../ui/forms.js";
import { toast } from "../ui/feedback.js";
import { renderError, setTitle } from "../ui/view.js";

export async function renderProfile() {
    const isUser = state.session.role === "USER";
    setTitle("Hồ sơ", isUser ? "Độc giả" : "Nhân viên");
    els.pageRoot.innerHTML = '<div class="page-stack"><div class="empty-state">Đang tải hồ sơ...</div></div>';
    try {
        const profile = unwrapData(await api((isUser ? "/api/reader/idReader=" : "/api/staff/idStaff=") + enc(state.session.idUser)));
        const fields = isUser ? userUpdateFields : staffUpdateFields;
        els.pageRoot.innerHTML = [
            '<div class="grid-2">',
            '<section class="surface"><div class="section-header"><div class="section-title"><h2>Thông tin cá nhân</h2><p>Cập nhật dữ liệu liên hệ và trạng thái tài khoản.</p></div></div><div class="drawer-body">',
            formHtml("profileForm", fields, profile, "Lưu hồ sơ"),
            '</div></section>',
            '<section class="surface"><div class="section-header"><div class="section-title"><h2>Ảnh đại diện</h2><p>Ảnh dùng trong hồ sơ và phiếu mượn.</p></div></div><div class="drawer-body"><form id="avatarForm" class="page-stack"><label class="field"><span>Ảnh</span><input name="avatar" type="file" accept="image/*" required></label><button class="button button-primary" type="submit">Cập nhật ảnh</button></form></div></section>',
            '</div>'
        ].join("");
        document.getElementById("profileForm").addEventListener("submit", async function (event) {
            event.preventDefault();
            const payload = formValues(event.currentTarget, fields);
            await submitJson("PUT", isUser ? "/api/reader" : "/api/staff", payload, "Đã cập nhật hồ sơ.");
        });
        document.getElementById("avatarForm").addEventListener("submit", async function (event) {
            event.preventDefault();
            const formData = new FormData();
            formData.append("idUser", state.session.idUser);
            formData.append("avatar", event.currentTarget.avatar.files[0]);
            const result = await api(isUser ? "/api/reader/avatar" : "/api/staff/avatar", { method: "PUT", body: formData });
            toast(result.message || "Đã cập nhật ảnh.", "success");
        });
    } catch (err) {
        renderError(err);
    }
}
