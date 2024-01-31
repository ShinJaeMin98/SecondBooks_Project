window.addEventListener("DOMContentLoaded", function(){
    const subjects = document.querySelectorAll(".items .subject");
    for(const el of subjects) {
        el.addEventListener("click", function(){
            const contentEl = this.nextElementSibling;
            if(contentEl.classList.contains("dn")){
            contentEl.classList.remove("dn")};
            } else{
                contentEl.classList.add("dn");
            }
        });
    }
});