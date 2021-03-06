<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="../ssi/ssi.jsp"%>
<jsp:useBean id="dao" class="address.AddressDAO" />
<%
	//검색관련
	String col = Utility.checkNull(request.getParameter("col"));
	String word = Utility.checkNull(request.getParameter("word"));

	if (col.equals("total"))
		word = "";

	//Paging 관련
	int nowPage = 1;
	int recordPerPage = 5;

	if (request.getParameter("nowPage") != null)
		nowPage = Integer.parseInt(request.getParameter("nowPage"));

	//DB에서 가져올 레코드의 시작번호 and 끝번호
	int sno = ((nowPage - 1) * recordPerPage) + 1;
	int eno = nowPage * recordPerPage;

	Map map = new HashMap();
	map.put("col", col);
	map.put("word", word);
	map.put("sno", sno);
	map.put("eno", eno);

	List<AddressDTO> list = dao.list(map);

	int totalRecord = dao.total(map);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<style type="text/css">
* {
	font-family: gulim;
	font-size: 20px;
}

div {
	text-align: center;
	margin: 10px 0px;
	padding: 5px;
}

table {
	witdh: 60%;
	margin: auto
}

table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
	padding: 5px;
}
</style>
<script type="text/javascript">
	function update(no) {
		//alert(no);
		var url = "updateForm.jsp";
		
		url = url + "?no=" + no;
		url = url + "&col=<%=col%>";
		url = url + "&word=<%=word%>";
		url = url + "&nowPage=<%=nowPage%>";


		location.href = url;
	}
	function read(no) {
		//alert(no);
		var url = "read.jsp";
		url = url + "?no=" + no;
		url = url + "&col=<%=col%>";
		url = url + "&word=<%=word%>";
		url = url + "&nowPage=<%=nowPage%>";

		location.href = url;

	}
	function del(no) {
		//alert(no);
		if (confirm("정말 삭제하시겠습니까?")) {
			var url = "delete.jsp";
			url = url + "?no=" + no;
			url = url + "&col=<%=col%>";
			url = url + "&word=<%=word%>";
			url = url + "&nowPage=<%=nowPage%>";

			location.href = url;
		}
	}
	function reply(no) {
		var url = "replyForm.jsp";
		url = url + "?no=" + no;
		url = url + "&col=<%=col%>";
		url = url + "&word=<%=word%>";
		url = url + "&nowPage=<%=nowPage%>";


		location.href = url;

	}
</script>


</head>
<body>
	<jsp:include page="/menu/top.jsp" />

	<form method="post" action="list.jsp">
	<div>주소목록</div>
	<div class="search">
		<select name="col">
			<option value="name"
				<%if (col.equals("name"))
				out.print("selected");%>>이름</option>
			<option value="phone"
				<%if (col.equals("phone"))
				out.print("selected");%>>전화번호</option>
			<option value="total">전체출력</option>
		</select> <input type="text" name="word" value="<%=word%>">
		<button>검색</button>
		<button type="button" onclick="location.href='createForm.jsp'">등록</button>
	</div>
	</form>
	<table>
		<tr>
			<th>번호</th>
			<th>이름</th>
			<th>전화번호</th>
			<th>grpno</th>
			<th>indent</th>
			<th>ansnum</th>
			<th>수정/삭제</th>
		</tr>
		<%
			for (int i = 0; i < list.size(); i++) {
				AddressDTO dto = list.get(i);
		%>
		<tr>

			<td><%=dto.getNo()%></td>
			<td>
				<%
					for (int r = 0; r < dto.getIndent(); r++)
							out.print("&nbsp;&nbsp");

						if (dto.getIndent() > 0)
							out.print("<img src='../menu/images/re.jpg'>");
				%> <a href="javascript:read('<%=dto.getNo()%>')"> <%=dto.getName()%></a>
			</td>
			<td><%=dto.getPhone()%></td>
			<td><%=dto.getGrpno()%></td>
			<td><%=dto.getIndent()%></td>
			<td><%=dto.getAnsnum()%></td>
			<td><a href="javascript:update('<%=dto.getNo()%>')">수정</a> / <a
				href="javascript:del('<%=dto.getNo()%>')">삭제</a> / <a
				href="javascript:reply('<%=dto.getNo()%>')">답변</a></td>
		</tr>
		<%
			}
		%>
	</table>
	<div>
		<%=Utility.paging3(totalRecord, nowPage, recordPerPage, col, word) %>

	</div>
	<jsp:include page="/menu/bottom.jsp" />
</body>
</html>
