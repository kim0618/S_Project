function daumPost(){	// 주소 검색하는 창 연결
    new daum.Postcode({
        oncomplete: function(data) {
        	// R : 도로명, J : 지번
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
            // 예제를 참고하여 다양한 활용법을 확인해 보세요.
            console.log("data.userSelectedType : "+data.userSelectedType)
            console.log("data.roadAddress : "+data.roadAddress)
            console.log("data.jibunAddress : "+data.jibunAddress)
            console.log("data.zonecode(우편번호) : "+data.zonecode)
            var addr ="";
  		  if(data.userSelectedType === 'R'){
  			  	addr = data.roadAddress
  		  }else{
   			 	addr = data.jibunAddress
   		 }
    	$('#addr1').val(data.zonecode)
    	$('#addr2').val(addr)
    	$('#addr3').focus()
        }
    }).open();
}