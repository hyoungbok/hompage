<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ include file="../ssi/ssi.jsp" %>
<jsp:useBean id="dao" class="address.AddressDAO"/>
<% 
	int no = Integer.parseInt(request.getParameter("no"));

	boolean dflag =dao.checkRefnum(no);

	boolean flag = false;
		if(dflag==false){
			flag=dao.delete(no);
		}
%>

<!DOCTYPE html> 
<html> 
<head> 
<meta charset="UTF-8"> 
<title></title> 
<style type="text/css"> 
*{ 
  font-family: gulim; 
  font-size: 20px; 
} 
div#title{
	width:30%;
	margin: 20px auto;
	margin-top: 20px;
	border: 1px solid black;
}
div{
	text-align:center;
	margin-top:10px;
}
</style>
<script type="text/javascript">
function tlist(){
	
	var url = "list.jsp";
	url = url + "?col=<%=request.getParameter("col")%>";
	url = url + "&word=<%=request.getParameter("word")%>";
	url = url + "&nowPage=<%=request.getParameter("nowPage")%>";
	
	location.href=url;
}
</script> 
</head> 
<body> 
<div id="title">처리결과</div>
<div>
<%
	if(dflag){
		out.print("답변글이 있으므로 삭제할 수 없습니다.");
	}else if(flag){
		out.print("삭제가 되었습니다.");
		}else{
			out.print("삭제가 실패했습니다.");
		} 
%>
</div>
<div id="title">
<button onclick="tlist()">목록</button>
</div>

</body> 
</html> 