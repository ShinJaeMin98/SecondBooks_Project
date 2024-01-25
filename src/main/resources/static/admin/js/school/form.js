window.addEventListener("DOMContentLoaded", function() {
     const dragDropBoxes = document.querySelectorAll(".dragndrop_box.uploaded");
        for (const el of dragDropBoxes) {
            el.addEventListener("dblclick", (e) => deleteDragDropImage(e.currentTarget.dataset.fileId));
        }
        /* 드래그 앤 드롭 삭제 이벤트 처리 E */
    });

function callbackFileUpload(files) {
    if (!files || files.length == 0) {
        return;
    }


           for (const file of files) {
               /* 드래그 앤 드롭 파일 처리 S */
               if (file.location.indexOf("banner") != -1) { // location 값에 banner가 포함되어 있으면

                   dragAndDropProcess(file);

                   continue;
               } else if (file.location == 'logo') {
                    logoImageProcess(file);
               }
           }
           /* 로고 이미지 처리 S */
           function logoImageProcess(file) {
                const schoolLogo = document.getElementById("school_logo");
                let html = document.getElementById("image1_tpl").innerHTML;


                html = html.replace(/\[seq\]/g, file.seq)
                            .replace(/\[imageUrl\]/g, file.fileUrl);

               schoolLogo.innerHTML = html;
           }
            /* 로고 이미지 처리 E */

           /* 드래그 앤 드롭 파일 처리 E */

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
           * 파일 삭제 후 후속 처리
           *
           * @param seq : 파일 등록 번호
           */

           function callbackFileDelete(seq) {
               const fileBox = document.getElementById(`file_${seq}`);

                if (fileBox.classList.contains("image1_tpl_box")) {
                   fileBox.parentElement.removeChild(fileBox);
                   return;
                }

               fileBox.classList.remove('uploaded');
               fileBox.style.backgroundImage = fileBox.style.backgroundPosition = fileBox.style.backgroundSize = null;
           }