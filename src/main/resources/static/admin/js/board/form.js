window.addEventListener("DOMContentLoaded", function() {
    const { loadEditor } = commonLib;


    loadEditor("html_top")
        .then(editor => window.editor1 = editor)
        .catch(err => console.error(err));

    loadEditor("html_bottom")
        .then(editor => window.editor2 = editor)
        .catch(err => console.error(err));
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

       editor.execute('insertImage', { source: file.fileUrl });

       /* 템플릿 데이터 치환 S */
        let html = tpl;
        html = html.replace(/\[seq\]/g, file.seq)
                    .replace(/\[fileName\]/g, file.fileName)
                    .replace(/\[imageUrl\]/g, file.fileUrl);

        const dom = domParser.parseFromString(html, "text/html");
        const fileBox = dom.querySelector(".file_tpl_box");
        target.appendChild(fileBox);

       /* 템플릿 데이터 치환 E */
    }
}