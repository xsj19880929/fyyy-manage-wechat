<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div style="padding:0px 20px;">
<div class="alert alert-error">
  <h4>出错了!</h4>
  ${exception.message}
  <p style="word-break:break-all;">
   ${exception.stackTrace}
  </p>
</div>
</div>