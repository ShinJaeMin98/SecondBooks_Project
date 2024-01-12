window.addEventListener("DOMContentLoaded", function() {
    const { loadEditor } = commonLib;


    loadEditor("html_top")
        .then(editor => window.editor1 = editor)
        .catch(err => console.error(err));

    loadEditor("html_bottom")
        .then(editor => window.editor2 = editor)
        .catch(err => console.error(err));

    /* 이미지 본문 추가 이벤트 처리 S */
    const insertImages = document.getElementsByClassName("insert_image");
    for (const el of insertImages) {
        el.addEventListener("click", function() {
            const parentId = this.parentElement.parentElement.id;
            const editor = parentId.indexOf("html_bottom") == -1 ? editor1 : editor2;
            const url = this.dataset.url;

            insertImage(editor, url);
        });
    }
    /* 이미지 본문 추가 이벤트 처리 E */

    /* 드래그 앤 드롭 삭제 이벤트 처리 S */
    const dragDropBoxes = document.querySelectorAll(".dragndrop_box.uploaded");
    for (const el of dragDropBoxes) {
        el.addEventListener("dblclick", (e) => deleteDragDropImage(e.currentTarget.dataset.fileId));
    }
    /* 드래그 앤 드롭 삭제 이벤트 처리 E */
});

/**
* 파일 업로드 후 후속처리 함수
*
*/
function callbackFileUpload(files) {
    if (!files || files.length == 0) {
        return;
    }

    const tpl = document.getElementById("editor_tpl").innerHTML;
    const domParser = new DOMParser();
    const targetTop = document.getElementById("uploaded_files_html_top");
    const targetBottom = document.getElementById("uploaded_files_html_bottom");

    for (const file of files) {
        /* 드래그 앤 드롭 파일 처리 S */
        if (file.location.indexOf("logo") != -1) { // location 값에 logo가 포함되어 있으면

            dragAndDropProcess(file);

            continue;
        }
        /* 드래그 앤 드롭 파일 처리 E */

       const editor = file.location == 'html_bottom' ? editor2 : editor1;
       const target = file.location == 'html_bottom' ? targetBottom : targetTop;

        insertImage(editor, file.fileUrl); // 에디터에 이미지 추가

       /* 템플릿 데이터 치환 S */
        let html = tpl;
        html = html.replace(/\[seq\]/g, file.seq)
                    .replace(/\[fileName\]/g, file.fileName)
                    .replace(/\[imageUrl\]/g, file.fileUrl);

        const dom = domParser.parseFromString(html, "text/html");
        const fileBox = dom.querySelector(".file_tpl_box");
        target.appendChild(fileBox);


        const el = fileBox.querySelector(".insert_image")
        if (el) {
            // 이미지 본문 추가 이벤트
            el.addEventListener("click", () => insertImage(editor, file.fileUrl));
        }

       /* 템플릿 데이터 치환 E */
    }

    /**
    * 드래그 앤 드롭 파일 업로드 처리
    *
    */
    function dragAndDropProcess(file) {
        const logoBox = document.querySelector(`.${file.location}_box`);

        const imageUrl = file.thumbsUrl.length > 0 ? file.thumbsUrl.pop() : file.fileUrl;

        logoBox.style.backgroundImage = `url('${imageUrl}')`;
        logoBox.style.backgroundRepeat = 'no-repeat';
        logoBox.style.backgroundPosition = 'center center';
        logoBox.style.backgroundSize = 'cover';

        logoBox.dataset.fileId = file.seq;
        logoBox.id = `file_${file.seq}`;

        if (!logoBox.classList.contains('uploaded')) { // 파일이 업로드된 상태가 아닌 경우, 업로드 상태로 변경
            logoBox.classList.add('uploaded');
        }


        /* 더블 클릭시 파일 삭제 처리 S */
        logoBox.addEventListener("dblclick", () => deleteDragDropImage(seq));
        /* 더블 클릭시 파일 삭제 처리 E */
    }
}

/**
* 드래그 앤 드롭 이미지 삭제
*
* @param seq : 파일 등록번호
*/
function deleteDragDropImage(seq) {
    if (!confirm('정말 삭제하겠습니까?')) {
        return;
    }

    const { fileManager } = commonLib;

    fileManager.delete(seq);

}


/**
* 에디터에 이미지 추가
*
*/
function insertImage(editor, source) {
    editor.execute('insertImage', { source });
}

/**
* 파일 삭제 후 후속 처리
*
* @param seq : 파일 등록 번호
*/
function callbackFileDelete(seq) {
    const fileBox = document.getElementById(`file_${seq}`);
    fileBox.classList.remove('uploaded');
    fileBox.style.backgroundImage = fileBox.style.backgroundPosition = fileBox.style.backgroundSize = null;
}