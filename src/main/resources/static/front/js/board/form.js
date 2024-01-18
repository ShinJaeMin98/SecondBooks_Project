window.addEventListener("DOMContentLoaded", function() {
    if (typeof ClassicEditor == 'function') { // 에디터 사용
        const { loadEditor } = commonLib;

        loadEditor("content", 450)
            .then(editor => window.editor = editor);
    } // 에디터 사용 E

    /* 이미지 본문 추가 이벤트 처리 S */
    const insertImages = document.getElementsByClassName("insert_image");
    for (const el of insertImages) {
        el.addEventListener("click", (e) => insertImage(e.currentTarget.dataset.url));
    }
    /* 이미지 본문 추가 이벤트 처리 E */
});


// 에디터에 이미지 추가
function insertImage(source) {
    editor.execute('insertImage', { source })
}

/**
* 파일 업로드 후속 처리
*
*/
function callbackFileUpload(files) {
    if (!files || files.length == 0) {
        return;
    }

    const imageUrls = [];

    const editorTpl = document.getElementById("editor_tpl").innerHTML;
    const attachTpl = document.getElementById("attach_tpl").innerHTML;

    const editorFiles = document.getElementById("editor_files");
    const attachFiles = document.getElementById("attach_files");

    const domParser = new DOMParser();

    for (const file of files) {
        const location = file.location;

        let html = location == 'editor' ? editorTpl : attachTpl;
        const targetEl = location == 'editor' ? editorFiles : attachFiles;

        if (location == 'editor') {
            imageUrls.push(file.fileUrl);
        }

        html = html.replace(/\[seq\]/g, file.seq)
                    .replace(/\[fileName\]/g, file.fileName)
                    .replace(/\[imageUrl\]/g, file.fileUrl);

        const dom = domParser.parseFromString(html, "text/html");
        const fileBox = dom.querySelector(".file_tpl_box");

        targetEl.appendChild(fileBox);

        const insertImageEl = fileBox.querySelector(".insert_image");

        if (insertImageEl) insertImageEl.addEventListener("click", () => insertImage(file.fileUrl));
    }

    if (imageUrls.length > 0) insertImage(imageUrls);

}

/**
* 파일 삭제후 후속 처리
*
*/
function callbackFileDelete(seq) {
    const fileBox = document.getElementById(`file_${seq}`);
    fileBox.parentElement.removeChild(fileBox);
}

const checkSell = function() {
  if (frmSave.category[0].checked) {
      frmSave.price.disabled=false;
  } else {
    frmSave.price.value='';
      frmSave.price.disabled=true;
  }
}