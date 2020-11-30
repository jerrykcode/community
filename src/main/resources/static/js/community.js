function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {

    if (!content) {
        alert("评论内容不能为空~~~");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            }
            else {
                if (response.code==2000) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=b0d8aca9afe1c249ac53&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                }
                alert(response.message);
            }
            console.log(response);
        },
        dataType: "json"
    });
}


function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId, 2, content);
}

function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    var icon = $("#icon-"+id);

    //获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠
        comments.removeClass("in");
        icon.removeClass("icon-open");
        e.removeAttribute("data-collapse");
    }
    else {
        //展开
        var subCommentContainer = $("#comment-"+id);
        if (subCommentContainer.children().length <= 1) {

            $.getJSON("/comment/" + id, function (data) {
               console.log(data);
               $.each(data.data.reverse(), function (index, comment) {

                    var mediaLeft = $("<div/>", {
                        "class":"media-left"
                    }).append($("<img/>", {
                          "class":"media-object img-rounded",
                          "src":comment.user.avatarUrl
                      }));

                    var mediaBody = $("<div/>", {
                        "class":"media-body"
                    }).append($("<h5/>", {
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>", {
                        "class":"menu"
                    })).append($("<div/>", {
                        "html":comment.content
                    })).append($("<span/>", {
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format("YYYY-MM-DD")
                    }));

                    var mediaElement = $("<div/>", {
                        "class":"media"
                    }).append(mediaLeft)
                        .append(mediaBody);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
               });
            });
       }
       comments.addClass("in");
       icon.addClass("icon-open");
       e.setAttribute("data-collapse", "in");
    }
}

function showSelectTag() {
    var err = $("#error");
    if (err) {
        err.hide();
    }
    $("#select-tag").show();
}

function selectTag(tag) {
   var text = $("#tag").val();

   if (text) {
       var index = 0;
       var appear = false; //记录tag是否已经作为一个独立的标签出现过
       while (true) {
           index = text.indexOf(tag, index); //tag字符串在text中出现的位置
           if (index == -1) break;
           //判断text中出现的tag是否是另一个标签的一部分
           //即tag的前一个和后一个字符都是逗号","或者没有字符时，才说明tag是一个独立的标签
           if ((index == 0 || text.charAt(index - 1) == ",")
               && (index + tag.length == text.length || text.charAt(index + tag.length) == ",")
              ) {
               appear = true;
               break;
           }
           index++; //用于搜索下一个出现位置
       }
       if (!appear) {
           //若tag没有作为一个独立的标签出现过
           $("#tag").val(text + ',' + tag);
      }
   }
   else {
       $("#tag").val(tag);
   }

}
