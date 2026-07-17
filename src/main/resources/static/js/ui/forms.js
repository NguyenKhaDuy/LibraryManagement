import { esc, escAttr, formatDateTimeInput } from "../core/utils.js";

export function formHtml(id, fields, values, submitLabel) {
    return '<form id="' + id + '" class="page-stack"><div class="form-grid">' + fieldsHtml(fields, values || {}) + '</div><div class="button-row"><button class="button button-primary" type="submit">' + esc(submitLabel) + '</button></div></form>';
}

export function fieldsHtml(fields, values) {
    values = values || {};
    return fields.filter(function (f) { return !f.hidden; }).map(function (f) {
        const value = values[f.name] != null ? values[f.name] : (f.value != null ? f.value : "");
        const attrs = (f.required ? " required" : "") + (f.readonly ? " readonly" : "");
        const fileAttrs = (f.accept ? ' accept="' + escAttr(f.accept) + '"' : "") + (f.multiple ? " multiple" : "");
        const cls = "field" + (f.wide || f.type === "textarea" ? " field-wide" : "");
        let input;
        if (f.type === "select") {
            input = '<select name="' + escAttr(f.name) + '"' + attrs + '><option value=""></option>' + (f.options || []).map(function (opt) {
                const optValue = typeof opt === "object" ? opt.value : opt;
                const optLabel = typeof opt === "object" ? opt.label : opt;
                return '<option value="' + escAttr(optValue) + '"' + (String(value) === String(optValue) ? " selected" : "") + '>' + esc(optLabel) + '</option>';
            }).join("") + '</select>';
        } else if (f.type === "textarea") {
            input = '<textarea name="' + escAttr(f.name) + '"' + attrs + '>' + esc(value || "") + '</textarea>';
        } else if (f.type === "datetime") {
            input = '<input name="' + escAttr(f.name) + '" type="datetime-local" value="' + escAttr(formatDateTimeInput(value)) + '"' + attrs + '>';
        } else if (f.type === "file") {
            const previewId = "preview-" + f.name;
            const existing = f.existingImages || [];
            const existingHtml = existing.length
                ? '<div class="image-preview-grid" data-existing-images>' + existing.map(function (src) {
                    return '<img class="image-thumb" src="' + src + '" alt="">';
                }).join("") + "</div>"
                : "";
            input = existingHtml + '<input name="' + escAttr(f.name) + '" type="file"' + fileAttrs + attrs + ' data-image-preview="' + escAttr(previewId) + '">' +
                '<div class="image-preview-grid" id="' + escAttr(previewId) + '" data-new-images></div>';
        } else {
            input = '<input name="' + escAttr(f.name) + '" type="' + escAttr(f.type || "text") + '" value="' + escAttr(value || "") + '"' + (f.type === "number" ? ' step="any"' : "") + attrs + '>';
        }
        return '<label class="' + cls + '"><span>' + esc(f.label) + '</span>' + input + '</label>';
    }).join("");
}

export function fieldInline(id, label, type) {
    return '<label class="field"><span>' + esc(label) + '</span><input id="' + escAttr(id) + '" type="' + escAttr(type || "text") + '"></label>';
}

export function selectInline(id, label, options, placeholder) {
    options = options || [];
    return '<label class="field"><span>' + esc(label) + '</span><select id="' + escAttr(id) + '"><option value="">' + esc(placeholder || "Tất cả") + '</option>' +
        options.map(function (opt) {
            const optValue = typeof opt === "object" ? opt.value : opt;
            const optLabel = typeof opt === "object" ? opt.label : opt;
            return '<option value="' + escAttr(optValue) + '">' + esc(optLabel) + "</option>";
        }).join("") + "</select></label>";
}

export function bindImagePreviews(root) {
    if (!root) return;
    root.querySelectorAll("[data-image-preview]").forEach(function (input) {
        const target = document.getElementById(input.dataset.imagePreview);
        if (!target) return;
        input.addEventListener("change", function () {
            target.innerHTML = "";
            Array.from(input.files || []).forEach(function (file) {
                if (!file.type.startsWith("image/")) return;
                const img = document.createElement("img");
                img.className = "image-thumb";
                img.alt = file.name;
                img.src = URL.createObjectURL(file);
                target.appendChild(img);
            });
        });
    });
}

export function lineEditorHtml(id, specs) {
    return '<div class="line-editor" id="' + id + '" data-specs="' + escAttr(specs.join("|")) + '"><div class="section-title"><h2>Danh sách sách</h2><p>Thêm từng dòng sách cho phiếu.</p></div><div data-lines></div><button class="button button-secondary" type="button" data-add-line>Thêm dòng</button></div>';
}

export function initLineEditor(id, defaults, rows) {
    const root = document.getElementById(id);
    const specs = root.dataset.specs.split("|").map(function (item) {
        const parts = item.split(":");
        return { name: parts[0], label: parts[1] };
    });
    const container = root.querySelector("[data-lines]");

    function addLine(values) {
        const merged = Object.assign({}, defaults || {}, values || {});
        const row = document.createElement("div");
        row.className = "line-row";
        row.innerHTML = specs.map(function (s) {
            return '<label class="field"><span>' + esc(s.label) + '</span><input name="' + escAttr(s.name) + '" value="' + escAttr(merged[s.name] == null ? "" : merged[s.name]) + '"></label>';
        }).join("") + '<button class="icon-button" type="button" aria-label="Xóa dòng">×</button>';
        row.querySelector("button").addEventListener("click", function () { row.remove(); });
        container.appendChild(row);
    }

    (rows && rows.length ? rows : [defaults || {}]).forEach(addLine);
    root.querySelector("[data-add-line]").addEventListener("click", function () { addLine(defaults || {}); });
}

export function collectLines(id, keys) {
    return Array.from(document.querySelectorAll("#" + id + " .line-row")).map(function (row) {
        const item = {};
        keys.forEach(function (key) {
            const input = row.querySelector('[name="' + key + '"]');
            const value = input ? input.value.trim() : "";
            item[key] = ["fine", "price", "quantity", "totalPrice"].indexOf(key) >= 0 && value !== "" ? Number(value) : value;
        });
        return item;
    }).filter(function (item) { return item.bookId; });
}

export function formValues(form, fields) {
    const payload = {};
    fields.forEach(function (f) {
        if (f.hidden) return;
        const input = form.querySelector('[name="' + f.name + '"]');
        if (!input || input.disabled) return;
        if (f.type === "file") return;
        const value = input.value.trim();
        if (value === "" && !f.required) return;
        payload[f.name] = f.type === "number" && value !== "" ? Number(value) : value;
    });
    return payload;
}

export function formDataValues(form, fields) {
    const payload = new FormData();
    fields.forEach(function (f) {
        if (f.hidden) return;
        const input = form.querySelector('[name="' + f.name + '"]');
        if (!input || input.disabled) return;
        if (f.type === "file") {
            Array.from(input.files || []).forEach(function (file) {
                payload.append(f.name, file);
            });
            return;
        }
        const value = input.value.trim();
        if (value === "" && !f.required) return;
        payload.append(f.name, value);
    });
    return payload;
}
