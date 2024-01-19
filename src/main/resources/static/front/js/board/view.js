window.addEventListener("DOMContentLoaded", function() {
    // 댓글 작성 후 URL에 comment_id=댓글 등록번호 -> hash 추가 -> 이동
    if (location.search.indexOf("comment_id=") != -1) {
    const searchParams = new URLSearchParams(location.search);

    const seq = searchParams.get("comment_id");
    //searchParams.delete("comment_id");

    //location.search = searchParams.toString();

    location.hash = '#comment_${seq}';
    }

    /* 댓글 수정 버튼 클릭 처리 S*/
    const editComments = document.getElementsByClassName("edit_comment");
    for (const el of editComments) {
        el.addEventListener("click", function() {
            const seq = this.dataset.seq;
            const targetEl = document.querySelector('#comment_${seq} .comment');
            const textAreaEl = targetEl.querySelector("textarea");
            if (textAreaEl) { // 댓글 수정 처리

            } else { // TextArea 생성

            }

        });
    }
    /* 댓글 수정 버튼 클릭 처리 E*/

});