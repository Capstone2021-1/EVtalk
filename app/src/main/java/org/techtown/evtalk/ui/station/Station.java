package org.techtown.evtalk.ui.station;

public class Station {
    private String staNm = "NULL"; // 충전소명
    private String staId = "NULL"; // 충전소ID
    private String chgerId = "NULL"; // 충전기ID
    private String chgerType = "NULL"; // 충전기 타입
    /*충전기 타입 01: DC차데모, 02: AC완속, 03: DC차데모 + AC3상, 04: DC콤보, 05: DC차데모 + DC콤보, 06: DC차데모 + AC3상, 07: AC3상*/
    private String addr = "NULL"; // 소재지 도로명 주소
    private double lat; // 위도
    private double lng; //경도
    private String useTime = "NULL"; // 이용가능시간
    private String busiId = "NULL"; // 기관ID
    private String busiNm = "NULL"; // 운영기관명
    private String busiCall = "NULL"; // 관리업체 전화번호
    private String stat = "NULL"; //충전기 상태
    /*충전기 상태 1: 통신이상, 2: 충전대기, 3: 충전중, 4: 운영중지, 5: 점검중, 9: 상태미확인*/
    private String statUpdDt = "NULL"; // 상태갱신일시
    /*충전기 상태 변경, 통신이상, 통신복구 일시*/
    private String powerType = "NULL"; // 충전기용량
    private String zcode = "NULL"; // 지역코드
    private String parkingFree = "NULL"; // 주차무료 여부
    private String note = "NULL"; // 충전소 안내
    private String limitYn = "NULL"; // 이용자 제한
    /*이용자 제한 Y: 제한 있음, N: 제한없음*/
    private String limitDetail = "NULL"; // 이용제한 사유
    private String delYn = "NULL"; // 삭제 여부
    /*충전기 정보 삭제 여부 (최근 삭제된 충전기 정보 제공)*/
    private String delDetail = "NULL"; // 충전기 정보 삭제 사유

    private String Seoul = "11"; // 행정구역코드 앞 2자리_서울


    public void setStaNm(String staNm) {
        this.staNm = staNm;
    }
    public void setStaId(String staId) {
        this.staId = staId;
    }
    public void setChgerId(String chgerId) {
        this.chgerId = chgerId;
    }
    public void setChgerType(String chgerType) {
        this.chgerType = chgerType;
    }
    public void setAddr(String addr){
        this.addr = addr;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public void setLng(double lng){
        this.lng = lng;
    }
    public void setUseTime(String useTime){
        this.useTime = useTime;
    }
    public void setBusiId(String busiId){
        this.busiId = busiId;
    }
    public void setBusiNm(String busiNm) {
        this.busiNm = busiNm;
    }
    public void setBusiCall(String busiCall){
        this.busiCall = busiCall;
    }
    public void setStat(String stat){
        this.stat = stat;
    }
    public void setStatUpdDt(String statUpdDt){
        this.statUpdDt = statUpdDt;
    }
    public void setPowerType(String powerType){
        this.powerType = powerType;
    }
    public void setZcode(String zcode){
        this.zcode = zcode;
    }
    public void setParkingFree(String parkingFree){
        this.parkingFree = parkingFree;
    }
    public void setNote(String note){
        this.note = note;
    }
    public void setLimitYn(String limitYn){
        this.limitYn = limitYn;
    }
    public void setLimitDetail(String limitDetail){
        this.limitDetail = limitDetail;
    }
    public void setDelYn(String delYn){
        this.delYn = delYn;
    }
    public void setDelDetail(String delDetail){
        this.delDetail = delDetail;
    }

    public String getStaNm(){
        return staNm;
    }
    public String getStaId(){
        return staId;
    }
    public String getChgerId(){
        return chgerId;
    }
    public String getChgerType(){
        return chgerType;
    }
    public String getAddr(){
        return addr;
    }
    public double getLat(){
        return lat;
    }
    public double getLng(){
        return lng;
    }
    public String getUseTime(){
        return useTime;
    }
    public String getBusiId(){
        return busiId;
    }
    public String getBusiNm(){
        return busiNm;
    }
    public String getBusiCall(){
        return busiCall;
    }
    public String getStat(){
        return stat;
    }
    public String getStatUpdDt(){
        return statUpdDt;
    }
    public String getPowerType(){
        return powerType;
    }
    public String getZcode(){
        return zcode;
    }
    public String getParkingFree(){
        return parkingFree;
    }
    public String getNote(){
        return note;
    }
    public String getLimitYn(){
        return limitYn;
    }
    public String getLimitDetail(){
        return limitDetail;
    }
    public String getDelYn(){
        return delYn;
    }
    public String getDelDetail(){
        return delDetail;
    }


    public String getSeoul(){
        return Seoul;
    }


}
