var commonLib = commonLib || {};
/**
* 1. 파일 업로드
*
*/
commonLib.fileManager = {
    /**
    * 파일 업로드 처리
    *
    * @param files : 업로드 파일 정보 목록
    * @param location : 파일 그룹(gid) 안에서 위치 구분 값(예 - 메인이미지, 목록이미지, 상세페이지 이미지)
    * @param imageOnly : true - 이미지만 업로드 가능하게 통제
    * @param singleFile : true - 단일 파일 업로드
    */
    upload(files, location, imageOnly, singleFile) {
        try {
            if (!files || files.length == 0) {
                throw new Error("업로드할 파일을 선택하세요.");
            }


            // gid
            const gidEl = document.querySelector("[name='gid']");
            if (!gidEl || !gidEl.value.trim()) {
                throw new Error("gid가 누락되었습니다.");
            }

            const gid = gidEl.value.trim();

            const formData = new FormData(); // 기본 Content-Type: multipart/form-data ...

            formData.append("gid", gid);

            if (location) {
                formData.append("location", location);
            }

            if (singleFile) {
                formData.append("singleFile", singleFile);
            }

            // 이미지만 업로드 가능일때 처리 S
            if (imageOnly) {
                for (const file of files) {
                    // 이미지 형식이 아닌 파일이 포함되어 있는 경우
                    if (file.type.indexOf("image/") == -1) {
                        throw new Error("이미지 형식의 파일만 업로드 가능합니다.");
                    }
                }

                formData.append("imageOnly", imageOnly);
            }
            // 이미지만 업로드 가능일때 처리 E

            for (const file of files) {
                formData.append("file", file);
            }

            const { ajaxLoad } = commonLib;
            ajaxLoad("POST", "/api/file", formData, "json")
                .then(res => { // 요청 성공시
                    if (res && res.success) { // 파일 업로드 성공시

                        if (typeof parent.callbackFileUpload == 'function') {
                            parent.callbackFileUpload(res.data);
                        }

                    } else { // 파일 업로드 실패시
                        if (res) alert(res.message);
                    }
                })
                .catch(err => console.error(err));

        } catch (err) {
            alert(err.message);
            console.error(err);
        }
    }
};


// 이벤트 처리
window.addEventListener("DOMContentLoaded", function() {
    const uploadFiles = document.getElementsByClassName("upload_files");

    // 파일 업로드 버튼 클릭 처리 -> 파일 탐색기 열기
    for (const el of uploadFiles) {
        el.addEventListener("click", function() {

           const fileEl = document.createElement("input");
           fileEl.type="file";
           fileEl.multiple = true; // 여러개 파일을 선택 가능하게

            const imageOnly = this.dataset.imageOnly == 'true';
            fileEl.imageOnly = imageOnly;
            fileEl.location = this.dataset.location;

            const singleFile = this.dataset.singleFile == 'true';
            fileEl.singleFile = singleFile;
            if (singleFile) fileEl.multiple = false;

            // 파일 선택시 이벤트 처리
            fileEl.addEventListener("change", function(e) {
              const imageOnly = fileEl.imageOnly || false;
              const location = fileEl.location;
              const singleFile = fileEl.singleFile;

              commonLib.fileManager.upload(e.target.files, location, imageOnly, singleFile);
            });

            fileEl.click();
        });
    }
});