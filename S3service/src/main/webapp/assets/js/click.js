// HTML5의 geolocation으로 사용할 수 있는지 확인합니다
function clickme() {
    if (navigator.geolocation) {

        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        navigator.geolocation.getCurrentPosition(function (position) {

            var lat = position.coords.latitude, // 위도
                lon = position.coords.longitude; // 경도

            var locPosition = new kakao.maps.LatLng(lat, lon); // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다

            //가져온 좌표를 통해 행정구역을 가져옵니다.
            let gu, dong;
            $.ajax({
                async: false, //전역변수에 담을려면 사용해야한다는데 왜그런지 찾아보기
                url: 'https://dapi.kakao.com/v2/local/geo/coord2address.json?x=' + lon + '&y=' + lat,
                type: 'GET',
                headers: {
                    'Authorization': 'KakaoAK 93beaa0136a7167723cdbb2eb52d47da'
                },
                success: function (data) {
                    //전체 console.log(data);
                    //서울  console.log(data.documents[0].address.region_1depth_name);
                    gu = data.documents[0].address.region_2depth_name; //구
                    dong = data.documents[0].address.region_3depth_name;  //동
                    console.log(data)


                }


            });
            var message = '<div style="padding:5px;" >' + gu + '<a href="/mapListInfo?dong=' + dong + '">' + dong + '</a>에 계신가요?</div>' // 인포윈도우에 표시될 내용입니다
            // 마커와 인포윈도우를 표시합니다


            displayMarker(locPosition, message);
        });

    } else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다

        var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),
            message = 'geolocation을 사용할수 없어요..'

        displayMarker(locPosition, message);
    }

}

// 지도에 마커와 인포윈도우를 표시하는 함수입니다
function displayMarker(locPosition, message) {

    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        map: map,
        position: locPosition
    });

    var iwContent = message, // 인포윈도우에 표시할 내용
        iwRemoveable = true;

    // 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({
        content: iwContent,
        removable: iwRemoveable
    });

    // 인포윈도우를 마커위에 표시합니다
    infowindow.open(map, marker);


}