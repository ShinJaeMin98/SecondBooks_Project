/**
* 파일 업로드 후속 처리
*
*/
function callbackFileUpload(files) {
    if (!files || files.length == 0) {
        return;
    }

    const file = files[0];

    const profileImage = document.querySelector(".profile_page .profile_image");
    profileImage.innerHTML = "";

    const deleteLink = document.createElement("a");
    const deleteIcon = document.createElement("i");
    deleteIcon.className="xi-close";
    deleteLink.href=`/file/delete/${file.seq}`;
    deleteLink.onclick = (e) => {
        if (!confirm('정말 삭제하겠습니까?')) {
            e.preventDefault();
        }
    };

    deleteLink.target="ifrmProcess";

    deleteLink.appendChild(deleteIcon);

    const img = new Image();
    img.src=file.fileUrl;
    img.width=250;

    profileImage.appendChild(deleteLink);
    profileImage.appendChild(img);
}


/**
* 파일 삭제 후속 처리
*
*/
function callbackFileDelete(seq) {
    const profileImage = document.querySelector(".profile_page .profile_image");
    console.log(profileImage);
    profileImage.innerHTML = "";
}