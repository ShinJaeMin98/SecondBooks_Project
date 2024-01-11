window.addEventListener("DOMContentLoaded", function() {
    const { loadEditor } = commonLib;


    loadEditor("html_top")
        .then(editor => window.editor1 = editor)
        .catch(err => console.error(err));

    loadEditor("html_bottom")
        .then(editor => window.editor2 = editor)
        .catch(err => console.error(err));

    /*  이미지 본문 추가 이벤트 처리 S */
    const insertImages = document.getElementsByClassName("insert_image");
    for( const el of insertImages){
        el.addEventListener("click", function(){
            const parentId = this.parentElement.parentElement.id;
            const editor = parentId.indexOf("html_bottom") == -1 ? editor1 : editor2;
            const url = this.dataset.url;

            insertImage(editor, url);
        });
    }
    /* 이미지 본문 추가 이벤트 처리 E */
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
        if(el){
            // 이미지 본문 추가 이벤트
            el.addEventListener("click", () => insertImage(editor, file.fileUrl));
        }

       /* 템플릿 데이터 치환 E */
    }
}

function insertImage(editor, source){
    editor.execute('insertImage', {source});
}