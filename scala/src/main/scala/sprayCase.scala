import spray.http.Uri

/**
 * @author Congpeixin
 * @date 2021/3/9 2:35 下午
 * @version 1.0
 * @describe
 */
object sprayCase {
  def main(args: Array[String]): Unit = {
    var uri = "/dsp/v1?data=eyJjbGlja191cmxzIjpbXSwiYXBwSW5mbyI6eyJoYXNoIjo3NTM3Njc5MzYwLCJz%0D%0AY29yZSI6MCwiYXBwTmFtZSI6IuW/jeiQjOaImOWnrCIsImFwcFNpemUiOjAsInN1%0D%0AcGVyY2xhc3MiOiJOU09iamVjdCIsImRvd25sb2FkVVJMIjoiaHR0cHM6XC9cL2l0%0D%0AdW5lcy5hcHBsZS5jb21cL2NuXC9hcHBcL2lkMTQzNjQ4MjQ0MiIsImRlYnVnRGVz%0D%0AY3JpcHRpb24iOiI8QlVBcHBJbmZvOiAweDFjMTQ3ZGMwMD4iLCJkZXNjcmlwdGlv%0D%0AbiI6IjxCVUFwcEluZm86IDB4MWMxNDdkYzAwPiIsImNvbW1lbnROdW0iOjAsImFw%0D%0AcGlkIjoiMTQzNjQ4MjQ0MiJ9LCJhcHBTaXplIjowLCJzdXBlcmNsYXNzIjoiTlNP%0D%0AYmplY3QiLCJpY29uIjp7IndpZHRoIjoxMDAsImltYWdlVVJMIjoiaHR0cDpcL1wv%0D%0AcDMtdHQuYnl0ZWNkbi5jblwvaW1nXC93ZWIuYnVzaW5lc3MuaW1hZ2VcLzIwMTkw%0D%0AMzE2NWQwZDQ5NzIzYmZmN2VmOTQ1MjViM2NjfjEwMHgxMDAuaW1hZ2UiLCJoZWln%0D%0AaHQiOjEwMH0sImV4dEluZm8iOiJ7XCJ1aWRcIjogOTU4ODEzMTE1NzQsIFwidmlk%0D%0AXCI6IFwiNzc4MzkwLDc3Mzg1MCw3NzYxNjEsNzcxNTYzLDc5MDg2Nyw3MTI1OTcs%0D%0ANzkwNjEwLDc3MDQ5OSw3NzM4OTgsNzc0MTExLDY1NzI4NCw3OTA2OTgsNzc3NDA3%0D%0ALDc3MjgwMSw3ODIzMjksNzU2Njg1LDc3Mzg1Niw3NzU0NzIsNzkxNDE3LDc4ODc3%0D%0AMCw3NzQxMTcsNzc3ODc0LDc5MDcwMlwiLCBcInJpdFwiOiA5MTI3MDc1ODksIFwi%0D%0Ab3JpdFwiOiA5MDAwMDAwMDAsIFwiYWRfdHlwZVwiOiAxLCBcImhlaWdodFwiOiAx%0D%0ANTAsIFwicGFja190aW1lXCI6IDE1NTI5ODk1OTguMTcyMTEzLCBcInByaWNpbmdc%0D%0AIjogOSwgXCJkZXZpY2VfdHlwZVwiOiBcImlQaG9uZSA3XCIsIFwicHJvbW90aW9u%0D%0AX3R5cGVcIjogMCwgXCJpc19zZGtcIjogdHJ1ZSwgXCJhcHBfbmFtZVwiOiBcIlxc%0D%0AdTYxYzJcXHU3NDAzXFx1NWUxZFwiLCBcInV1aWRcIjogXCJcIiwgXCJvc192ZXJz%0D%0AaW9uXCI6IFwiMTEuMy4xXCIsIFwiY3JlYXRpdmVfaWRcIjogMTYyODI0ODEyOTQ1%0D%0AMTAyMCwgXCJ2ZXJzaW9uX2NvZGVcIjogXCI3LjEuMFwiLCBcInV1aWRfbWQ1XCI6%0D%0AIFwiXCIsIFwid2lkdGhcIjogNjAwLCBcInRlbXBsYXRlX3JhdGVcIjogMCwgXCJp%0D%0AbWdfZ2VuX3R5cGVcIjogMCwgXCJjbGllbnRfaXBcIjogXCIxMTQuMjUxLjIyOC45%0D%0AMFwiLCBcImFkX2lkXCI6IDE2MjgyMzE2Mjc4MzQzOTcsIFwiY29udmVydF9pZFwi%0D%0AOiAxNjI3OTYwOTE5NjQ4Mjk2LCBcImltZ19tZDVcIjogXCJcIiwgXCJhZF9wcmlj%0D%0AZVwiOiBcIlhJSVYwZ0FBQWlKY2doWFNBQUFDSWliMkdoajJGam1sU1d5OU1RXCIs%0D%0AIFwiYXBwX2lkXCI6IFwiNTAxMjcwN1wiLCBcInNvdXJjZV90eXBlXCI6IDEsIFwi%0D%0AbWFjXCI6IFwiMDI6MDA6MDA6MDA6MDA6MDBcIiwgXCJpZGZhXCI6IFwiRTNDNzQ2%0D%0AREQtQzc1NS00Rjk0LTkyQzctODBFRjAwRjUwQzM4XCIsIFwidWdfY3JlYXRpdmVf%0D%0AaWRcIjogXCJcIiwgXCJkZXZpY2VfaWRcIjogNTAwNTE1MjM2OTksIFwibGFuZ3Vh%0D%0AZ2VcIjogXCJnb2xhbmdcIiwgXCJjaWRcIjogMTYyODI0ODEyOTQ1MTAyMCwgXCJ1%0D%0AdFwiOiAxMiwgXCJpbnRlcmFjdGlvbl90eXBlXCI6IDQsIFwib3Blbl91ZGlkXCI6%0D%0AIFwiXCIsIFwicG9zXCI6IDMsIFwicmVxX2lkXCI6IFwiOTI4REI4OUQtQUMxNy00%0D%0ANkZDLTk3RTQtOEQzNURBMUEwNkQxdTUxMzNcIiwgXCJpc19kc3BfYWRcIjogZmFs%0D%0Ac2UsIFwiYWRfc2xvdF90eXBlXCI6IDUsIFwib3NfdHlwZVwiOiBudWxsLCBcIm9z%0D%0AXCI6IFwiaW9zXCIsIFwidGVtcGxhdGVfaWRcIjogMH0iLCJzY29yZSI6MCwiQWRU%0D%0AaXRsZSI6IuW/jeiQjOaImOWnrCIsImJ1dHRvblRleHQiOiLnq4vljbPkuIvovb0i%0D%0ALCJjb3VudERvd24iOjAsImltYWdlTW9kZSI6MiwiaW5BcHAiOnRydWUsIkFkRGVz%0D%0AY3JpcHRpb24iOiLpooTnuqbotoU0MDDkuIfnmoTml6Xns7vljaHniYzvvIzku4rm%0D%0Al6VJT1PmraPlvI/lhazmtYvvvIzpgIFTU1Poi7Hpm4TvvIEiLCJleHBpcmVUaW1l%0D%0Ac3RhbXAiOjAsImNvbW1lbnROdW0iOjAsIkFkSUQiOiIxNjI4MjQ4MTI5NDUxMDIw%0D%0AIiwiZGVidWdEZXNjcmlwdGlvbiI6IjxCVU1hdGVyaWFsTWV0YTogMHgxYzAxYWFk%0D%0ANDA%2BIiwic2hvd191cmxzIjpbXSwiaW1hZ2VBcnkiOlt7IndpZHRoIjo0NTYsImlt%0D%0AYWdlVVJMIjoiaHR0cDpcL1wvc2YxLXR0Y2RuLXRvcy5wc3RhdHAuY29tXC9pbWdc%0D%0AL3dlYi5idXNpbmVzcy5pbWFnZVwvMjAxOTAzMTQ1ZDBkYmFmMmUyNDZiOWNjNGQx%0D%0ANzljZDZ%2BY3NfNDU2eDMwMF9xODAuanBlZyIsImhlaWdodCI6MzAwfV0sImhhc2gi%0D%0AOjc1MTc5NDEwNTYsInByZWxvYWRlclRpbWVzdGFtcCI6MCwiZmlsdGVyV29yZHMi%0D%0AOlt7ImRpc2xpa2VJRCI6IjQ6MiIsImhhc2giOjc1MjA2MDc2NDgsInN1cGVyY2xh%0D%0Ac3MiOiJOU09iamVjdCIsImRlYnVnRGVzY3JpcHRpb24iOiI8QlVEaXNsaWtlV29y%0D%0AZHM6IDB4MWMwNDM1ZGEwPiIsImRlc2NyaXB0aW9uIjoiPEJVRGlzbGlrZVdvcmRz%0D%0AOiAweDFjMDQzNWRhMD4iLCJuYW1lIjoi55yL6L%2BH5LqGIiwiaXNTZWxlY3RlZCI6%0D%0AZmFsc2V9XSwiaW50ZXJhY3Rpb25UeXBlIjo0LCJzY3JlZW5zaG90IjpmYWxzZSwi%0D%0AdW5pb25TcGVjaWFsIjoyLCJkZXNjcmlwdGlvbiI6IjxCVU1hdGVyaWFsTWV0YTog%0D%0AMHgxYzAxYWFkNDA%2BIn0%3D&origin=jrtt"
    //        uri = "https://test-dab.dongqiudi.com/dsp/v1?imei=990007180166651&origin=jrtt&data=eyJjbGlja191cmwiOiJodHRwczpcL1wvd3d3LmNoZW5nemlqaWFuemhhbi5jb21cL3RldHJpc1wv%0AcGFnZVwvNjY3MjYyNDIyMTY4MTYxNDg2MlwvP2FkX2lkPTE2MjkxNDc4NzU3NDM3NDgmX3RvdXRp%0AYW9fcGFyYW1zPSU3QiUyMmNpZCUyMiUzQTE2MjkxNDg2MzE0NjU5OTIlMkMlMjJkZXZpY2VfaWQl%0AMjIlM0EzMzE0NjkwMTk5OCUyQyUyMmxvZ19leHRyYSUyMiUzQSUyMiU3QiU1QyUyMmFkX3ByaWNl%0AJTVDJTIyJTNBJTVDJTIyWEpuaWlRQUJUT05jbWVLSkFBRk00NmZPNEh1MjRmdy1qM1F4clElNUMl%0AMjIlMkMlNUMlMjJjb252ZXJ0X2lkJTVDJTIyJTNBMTYyOTEzNzE2Mzg5NTgyMCUyQyU1QyUyMm9y%0AaXQlNUMlMjIlM0E5MDAwMDAwMDAlMkMlNUMlMjJyZXFfaWQlNUMlMjIlM0ElNUMlMjIxZDZkMDZm%0AOS03MmMzLTRiM2UtOWI4OS03ZWEzNmZlMTVmZjN1NTA0OCU1QyUyMiUyQyU1QyUyMnJpdCU1QyUy%0AMiUzQTkxMzM4OTY2NCU3RCUyMiUyQyUyMm9yaXQlMjIlM0E5MDAwMDAwMDAlMkMlMjJyZXFfaWQl%0AMjIlM0ElMjIxZDZkMDZmOS03MmMzLTRiM2UtOWI4OS03ZWEzNmZlMTVmZjN1NTA0OCUyMiUyQyUy%0AMnJpdCUyMiUzQTkxMzM4OTY2NCUyQyUyMnNpZ24lMjIlM0ElMjJENDFEOENEOThGMDBCMjA0RTk4%0AMDA5OThFQ0Y4NDI3RSUyMiUyQyUyMnVpZCUyMiUzQTUxODg5MTc2NTE1JTJDJTIydXQlMjIlM0Ex%0AMiU3RCZhcHBlbmQ9JTdCJTIyb3BlbnVybCUyMiUzQSUyMiUyMiUyQyUyMnBvc3RkYXRhJTIyJTNB%0AJTVCJTdCJTIyX190eXBlX18lMjIlM0ElMjJyZXFfaWQlMjIlMkMlMjJjaWQlMjIlM0ExNjI5MTQ4%0ANjMxNDY1OTkyJTJDJTIycmVxX2lkJTIyJTNBJTIyMWQ2ZDA2ZjktNzJjMy00YjNlLTliODktN2Vh%0AMzZmZTE1ZmYzdTUwNDglMjIlMkMlMjJyaXQlMjIlM0E5MTMzODk2NjQlN0QlNUQlN0QiLCJpbWdf%0AdXJsIjoiaHR0cDpcL1wvc2YxLXR0Y2RuLXRvcy5wc3RhdHAuY29tXC9pbWdcL3dlYi5idXNpbmVz%0Acy5pbWFnZVwvMjAxOTAzMTU1ZDBkMDI0MGI1MmFkOWE3NDk1OWI2NGJ-Y3NfNDU2eDMwMF9xODAu%0AanBlZyIsInRpdGxlIjoi5Lit5Y2O5LiH5bm05Y6GLeaAp-iDveaPkOWNhyJ9%0A&"
    //        uri = "https://test-dab.dongqiudi.com/dsp/v1?data=eyJybCI6Imh0dHBzOlwvXC9zYy5nZHQucXEuY29tXC9nZHRfbWNsaWNrLmZjZz92%0D%0AaWV3aWQ9OUVxUm04SmdQQ2Y2WHVVOVViUFhDU19xWGZmUFpIRXJma1ZQTHYzWEVX%0D%0ATDZUOG45S29TZTBGMl81SXhNN21rNDhidFB1YmpjcExENll5QkRvQ2p0SXZ0ZHBW%0D%0ANWlVT1U4Xzl0emhpRlVVbERiamxtQlZ2NHQ1IWEwTWQ1bm5UN2lmeTVNeEpLS0g2%0D%0Ab2RQTG1CTkp3bVBaOUdNbHVHa2wySDFEUG1mSVBVVjJkaVVvM3NKcUhaTzN1Y29n%0D%0AbU8xMWxad0NQZF96WmlScTNZUEwydzBUekNoYU4yYm1mNTNUWFMmanR5cGU9MCZp%0D%0APTEmb3M9MSZhc2U9MSZ4cD0xIiwicHJvZHVjdFR5cGUiOjE5LCJjbCI6IjEwODkw%0D%0AMzQ5NiIsInRyYWNlSWQiOiI1M3JvM2lqazNnajJxMDEiLCJpdHVuZXNJZCI6IjM5%0D%0ANTA5NjczNiIsImlzVGhyZWVJbWdzQWQiOmZhbHNlLCJwcm9wZXJ0aWVzIjp7InRp%0D%0AdGxlIjoi5Y675ZOq5YS/5peF6KGMIiwiaWNvbiI6Imh0dHBzOlwvXC9wZ2R0LnVn%0D%0AZHRpbWcuY29tXC9nZHRcLzBcL0RBQUp3OEpBRXNBRXNBQXdCY20wUTlCYzhmS0VL%0D%0AaS5qcGdcLzA/Y2s9Mzg4ODFiZjFmNmM5OTliNDcxZTIyMTc1YTBiNjI2YjMiLCJp%0D%0AbWciOiJodHRwczpcL1wvcGdkdC51Z2R0aW1nLmNvbVwvZ2R0XC8wXC9EQUFKdzhK%0D%0AQVVBQUxRQUJDQmNtMFAxRFYxbWtvb24uanBnXC8wP2NrPThmMjI3ZmViNDRlMGQz%0D%0AYmE5OWQ1M2MzZjdmY2U4YTVlIiwiZGVzYyI6Iue9kee6oumrmOmTgee6v%2B%2B8jOay%0D%0Av%2BmAlOeahuaYr%2Be%2BjuaZr%2B%2B8jOW4puS4iuW/g%2BeIseeahOWlue%2B8jOadpeS4gOWc%0D%0AuuivtOi1sOWwsei1sOeahOaXheihjCIsInJhdGluZyI6NX0sImFwcG5hbWUiOiLl%0D%0Ajrvlk6rlhL/ml4XooYwiLCJpc0FwcEFkIjp0cnVlLCJhcFVybCI6Imh0dHBzOlwv%0D%0AXC92Mi5nZHQucXEuY29tXC9nZHRfc3RhdHMuZmNnP3ZpZXdpZD05RXFSbThKZ1BD%0D%0AZjZYdVU5VWJQWENTX3FYZmZQWkhFcmZrVlBMdjNYRVdMNlQ4bjlLb1NlMEYyXzVJ%0D%0AeE03bWs0OGJ0UHViamNwTEQ2WXlCRG9DanRJdnRkcFY1aVVPVThfOXR6aGlGVVVs%0D%0ARGJqbG1CVnY0dDUhYTBNZDVublQ3aWZ5NU14SktLSDZvZFBMbUJOSndtUFo5R01s%0D%0AdUdrbDJIMURQbWZJUFVWMmRpVW8zc0pxSFpPM3Vjb2dtTzExbFp3Q1BkX3paaVJx%0D%0AM1lQTDJ3MFR6Q2hhTjJibWY1M1RYUyZpPTEmb3M9MSZ4cD0xIiwiaXNFeHBvc3Vy%0D%0AZWQiOnRydWV9&origin=gdt"
    // 广点通iOS信息流
    uri = "https://test-dab.dongqiudi.com/dsp/v1?data=eyJybCI6Imh0dHBzOlwvXC9zYy5nZHQucXEuY29tXC9nZHRfbWNsaWNrLmZjZz92%0D%0AaWV3aWQ9bGZJZnJFNHQySEt5dmZfU09vZCFtNFFfMThlWHJVQk5wNlRwbmNZYWZn%0D%0AWjhRdnMhckRQZVhGMl81SXhNN21rNDhidFB1YmpjcExENll5QkRvQ2p0SXZ0ZHBW%0D%0ANWlVT1U4Xzl0emhpRlVVbERiamxtQlZ2NHQ1MThrcThEYjZtR0lmeTVNeEpLS0g2%0D%0Ab2RQTG1CTkp3bVBaOUdNbHVHa2wySDFEUG1mSVBVVjJjYXFtUmQyQ3FiU251Y29n%0D%0AbU8xMWxad0NQZF96WmlScTNuSnpyS0NlbzdfNk4yYm1mNTNUWFMmanR5cGU9MCZp%0D%0APTEmb3M9MSZhc2U9MSZ4cD0xIiwicHJvZHVjdFR5cGUiOjEwMDAsImNsIjoiOTg0%0D%0AMDc3NDQiLCJ0cmFjZUlkIjoiNXMydmZnaGY1a213dTAyIiwiaXR1bmVzSWQiOiIi%0D%0ALCJpc1RocmVlSW1nc0FkIjpmYWxzZSwicHJvcGVydGllcyI6eyJ0aXRsZSI6IuiH%0D%0AquWmguenn%2BaIv%2B%2B8jOaLjuWMheWFpeS9j%2B%2B8jOaIv%2Benn%2BaciOS7mCIsImljb24i%0D%0AOiJodHRwczpcL1wvcGdkdC51Z2R0aW1nLmNvbVwvZ2R0XC8wXC9EQUFHYjRpQUVz%0D%0AQUVzQUFTQmNJTDRoQ1hpWWlPaDAuanBnXC8wP2NrPTRjN2I2Mzc5YTJhOGQxMzgx%0D%0AZTI3MzRmMDdiYzM5MjdiIiwiaW1nIjoiaHR0cHM6XC9cL3BnZHQudWdkdGltZy5j%0D%0Ab21cL2dkdFwvMFwvREFBR2I0aUFVQUFMUUFCZUJjWW9haEFRaV9QZDl0LmpwZ1wv%0D%0AMD9jaz1lYjQxMjQxOGQyNTJhMDkzNTZiZDAyZWE5MWMzNWQ1MyIsImRlc2MiOiLk%0D%0AuIrnj63ml4/npo/pn7PvvIHotoXlpKfpo5jnqpfvvIzpmLPlhYnnm7TlsITvvIzp%0D%0Aq5jlk4HotKjkvY/miL/mnaXoh6rlpoIifSwiYXBwbmFtZSI6IuiHquWmguWuoiIs%0D%0AImlzQXBwQWQiOmZhbHNlLCJhcFVybCI6Imh0dHBzOlwvXC92Mi5nZHQucXEuY29t%0D%0AXC9nZHRfc3RhdHMuZmNnP3ZpZXdpZD1sZklmckU0dDJIS3l2Zl9TT29kIW00UV8x%0D%0AOGVYclVCTnA2VHBuY1lhZmdaOFF2cyFyRFBlWEYyXzVJeE03bWs0OGJ0UHViamNw%0D%0ATEQ2WXlCRG9DanRJdnRkcFY1aVVPVThfOXR6aGlGVVVsRGJqbG1CVnY0dDUxOGtx%0D%0AOERiNm1HSWZ5NU14SktLSDZvZFBMbUJOSndtUFo5R01sdUdrbDJIMURQbWZJUFVW%0D%0AMmNhcW1SZDJDcWJTbnVjb2dtTzExbFp3Q1BkX3paaVJxM25KenJLQ2VvN182TjJi%0D%0AbWY1M1RYUyZpPTEmb3M9MSZ4cD0xIiwiaXNFeHBvc3VyZWQiOnRydWV9&origin=gdt"
    uri = "https://test-dab.dongqiudi.com/dsp/v1?data=eyJybCI6Imh0dHBzOlwvXC9zYy5nZHQucXEuY29tXC9nZHRfbWNsaWNrLmZjZz92%0D%0AaWV3aWQ9YTJfR09EbyFsVUtuZzZya1pZMEhlem9DR2tWeW1iUGUwMWt0X3h5bG4z%0D%0AIWcwbUcydG01cjdsMl81SXhNN21rNDhidFB1YmpjcExENll5QkRvQ2p0SXZ0ZHBW%0D%0ANWlVT1U4Xzl0emhpRlVVbERiamxtQlZ2NHQ1OWR5IXNFS0luUXJmeTVNeEpLS0g2%0D%0Ab2RQTG1CTkp3bVBaOUdNbHVHa2wySDFEUG1mSVBVVjJkaVVvM3NKcUhaTzN1Y29n%0D%0AbU8xMWxad0NQZF96WmlScTJPWFIyQ0VJMnA2Y1FwM1BlWkNGbVUmanR5cGU9MCZp%0D%0APTEmb3M9MSZhc2U9MSZ4cD0xIiwicHJvZHVjdFR5cGUiOjE5LCJjbCI6IjEwODkw%0D%0AMzQ5NiIsInRyYWNlSWQiOiI1M2M0aXJhYnR4d2FnMDIiLCJpdHVuZXNJZCI6IjM5%0D%0ANTA5NjczNiIsImlzVGhyZWVJbWdzQWQiOmZhbHNlLCJwcm9wZXJ0aWVzIjp7InRp%0D%0AdGxlIjoi5Y675ZOq5YS/5peF6KGMIiwiaWNvbiI6Imh0dHBzOlwvXC9wZ2R0LnVn%0D%0AZHRpbWcuY29tXC9nZHRcLzBcL0RBQUp3OEpBRXNBRXNBQXdCY20wUTlCYzhmS0VL%0D%0AaS5qcGdcLzA/Y2s9Mzg4ODFiZjFmNmM5OTliNDcxZTIyMTc1YTBiNjI2YjMiLCJp%0D%0AbWciOiJodHRwczpcL1wvcGdkdC51Z2R0aW1nLmNvbVwvZ2R0XC8wXC9EQUFKdzhK%0D%0AQVVBQUxRQUJDQmNtMFAxRFYxbWtvb24uanBnXC8wP2NrPThmMjI3ZmViNDRlMGQz%0D%0AYmE5OWQ1M2MzZjdmY2U4YTVlIiwiZGVzYyI6Iue9kee6oumrmOmTgee6v%2B%2B8jOay%0D%0Av%2BmAlOeahuaYr%2Be%2BjuaZr%2B%2B8jOW4puS4iuW/g%2BeIseeahOWlue%2B8jOadpeS4gOWc%0D%0AuuivtOi1sOWwsei1sOeahOaXheihjCIsInJhdGluZyI6NX0sImFwcG5hbWUiOiLl%0D%0Ajrvlk6rlhL/ml4XooYwiLCJpc0FwcEFkIjp0cnVlLCJhcFVybCI6Imh0dHBzOlwv%0D%0AXC92Mi5nZHQucXEuY29tXC9nZHRfc3RhdHMuZmNnP3ZpZXdpZD1hMl9HT0RvIWxV%0D%0AS25nNnJrWlkwSGV6b0NHa1Z5bWJQZTAxa3RfeHlsbjMhZzBtRzJ0bTVyN2wyXzVJ%0D%0AeE03bWs0OGJ0UHViamNwTEQ2WXlCRG9DanRJdnRkcFY1aVVPVThfOXR6aGlGVVVs%0D%0ARGJqbG1CVnY0dDU5ZHkhc0VLSW5RcmZ5NU14SktLSDZvZFBMbUJOSndtUFo5R01s%0D%0AdUdrbDJIMURQbWZJUFVWMmRpVW8zc0pxSFpPM3Vjb2dtTzExbFp3Q1BkX3paaVJx%0D%0AMk9YUjJDRUkycDZjUXAzUGVaQ0ZtVSZpPTEmb3M9MSZ4cD0xIiwiaXNFeHBvc3Vy%0D%0AZWQiOnRydWV9&origin=gdt"
    // 广点通iOS启动图
    //        uri = "https://test-dab.dongqiudi.com/dsp/v1?data=eyJjbGlja1VybCI6Imh0dHBzOlwvXC9zYy5nZHQucXEuY29tXC9nZHRfbWNsaWNr%0D%0ALmZjZz92aWV3aWQ9SmJLZ0VqeiE4VGpmeTRkZDFXIVk0ZlNJYlVsb0VQWUluSTJm%0D%0AVkFBaGZJYThrSktrVlpia3o1IUlNeDhmb2E3S05BMCFPRkx3TjlRV3MzUndwbSFT%0D%0AcGk4SFBJbnh2eWpPazNTVE4ybzlDV3Z3RUxzU2JYanpaYTZuNGhFNFFSckRmX1BP%0D%0Aa1NGX0dvSXZOQkZEaTUhVzYxMkRlenVoX0k1MFRjIVRoTlFCZ2g3REUwdDl3UkNh%0D%0ARGNBOV93QzZVelRnbCFfNDRrM0ZXSl9tYU1yNmZ6eTVzIWkwbFM4M29sX3cmanR5%0D%0AcGU9MCZpPTEmb3M9MSZhc2U9MSZ4cD0xIiwiaW1nVXJsIjoiaHR0cHM6XC9cL3Bn%0D%0AZHQudWdkdGltZy5jb21cL2dkdFwvMFwvREFBZkF6V0FLQUFQQUFCQkJjbXdEb0ND%0D%0AaExnU1BNLmpwZ1wvMD9jaz0zMGQ3Yjc3ZTQ0YTRiNWZhZDE4NDMyMGU3MWMxNzNj%0D%0AMyJ9&origin=gdt"
    //今日头条
    uri = "/dsp/v1?data=eyJzb3VyY2UiOiLkuIDliJnmlZnogrIiLCJ0YXJnZXRVUkwiOiJodHRwOlwvXC96%0D%0AejEuem9vbHVja3kuY25cLz9hZF9pZD0xNjMyMzk4NDk0NDk1Nzk2Jl90b3V0aWFv%0D%0AX3BhcmFtcz0lN0IlMjJjaWQlMjIlM0ExNjMyMzk4OTc4MzYzNDIwJTJDJTIyZGV2%0D%0AaWNlX2lkJTIyJTNBODYxMjc4OTEwNzM3Mjg5NiUyQyUyMmxvZ19leHRyYSUyMiUz%0D%0AQSUyMiU3QiU1QyUyMmFkX3ByaWNlJTVDJTIyJTNBJTVDJTIyWE1nYW1RQUdQLWxj%0D%0AeUJxWkFBWV82U01LSUtfaEVYamROUkJSVWclNUMlMjIlMkMlNUMlMjJjb252ZXJ0%0D%0AX2lkJTVDJTIyJTNBMTYzMjEzMDI0MzIzNTg0MyUyQyU1QyUyMm9yaXQlNUMlMjIl%0D%0AM0E5MDAwMDAwMDAlMkMlNUMlMjJyZXFfaWQlNUMlMjIlM0ElNUMlMjI5QTExNEEz%0D%0ARC1GQUM3LTQ5QzgtODVGRC1BNjAyRkI0RjgwNzl1NjcyMCU1QyUyMiUyQyU1QyUy%0D%0AMnJpdCU1QyUyMiUzQTkxMjcwNzU4OSU3RCUyMiUyQyUyMm9yaXQlMjIlM0E5MDAw%0D%0AMDAwMDAlMkMlMjJyZXFfaWQlMjIlM0ElMjI5QTExNEEzRC1GQUM3LTQ5QzgtODVG%0D%0ARC1BNjAyRkI0RjgwNzl1NjcyMCUyMiUyQyUyMnJpdCUyMiUzQTkxMjcwNzU4OSUy%0D%0AQyUyMnNpZ24lMjIlM0ElMjJENDFEOENEOThGMDBCMjA0RTk4MDA5OThFQ0Y4NDI3%0D%0ARSUyMiUyQyUyMnVpZCUyMiUzQTg2MTI3ODkxMDczNzI4OTYlMkMlMjJ1dCUyMiUz%0D%0AQTE0JTdEJmFwcGVuZD0lN0IlMjJvcGVudXJsJTIyJTNBJTIyJTIyJTJDJTIycG9z%0D%0AdGRhdGElMjIlM0ElNUIlN0IlMjJfX3R5cGVfXyUyMiUzQSUyMnJlcV9pZCUyMiUy%0D%0AQyUyMmNpZCUyMiUzQTE2MzIzOTg5NzgzNjM0MjAlMkMlMjJyZXFfaWQlMjIlM0El%0D%0AMjI5QTExNEEzRC1GQUM3LTQ5QzgtODVGRC1BNjAyRkI0RjgwNzl1NjcyMCUyMiUy%0D%0AQyUyMnJpdCUyMiUzQTkxMjcwNzU4OSU3RCU1RCU3RCIsImNsaWNrX3VybHMiOltd%0D%0ALCJhcHBTaXplIjowLCJzdXBlcmNsYXNzIjoiTlNPYmplY3QiLCJpY29uIjp7Indp%0D%0AZHRoIjozMDAsImltYWdlVVJMIjoiaHR0cDpcL1wvc2YxLXR0Y2RuLXRvcy5wc3Rh%0D%0AdHAuY29tXC9pbWdcL3dlYi5idXNpbmVzcy5pbWFnZVwvMjAxOTAyMjY1ZDBkMTlk%0D%0AMmY1YjdkYzYyNGQ1NWE2OGJ%2BY3NfMzAweDMwMF9xODAuanBlZyIsImhlaWdodCI6%0D%0AMzAwfSwiZXh0SW5mbyI6IntcInVpZFwiOiA4NjEyNzg5MTA3MzcyODk2LCBcInZp%0D%0AZFwiOiBcIjc3Mzg1MCw4NTI3NTcsODA1MjMwLDc3MDQ5NSw3NzM4OTEsNzc0MTEw%0D%0ALDY1NzI4Miw4NDIxMDMsODY0NDc4LDc5MDY5OCw4NjgwNTUsODY1Njk1LDc4MjMy%0D%0AOSw4NDk1MTksODYzNjQwLDg2NzU0OCw4Mzk5NTUsNzczODU1LDg2ODA2NSw4MzY3%0D%0AMDIsODY1OTI0LDg1OTM0MCw4NTA3MTYsNzc0MTE3LDc3Nzg3NCw4MzY3MzFcIiwg%0D%0AXCJyaXRcIjogOTEyNzA3NTg5LCBcIm9yaXRcIjogOTAwMDAwMDAwLCBcImFkX3R5%0D%0AcGVcIjogMSwgXCJoZWlnaHRcIjogMTUwLCBcInBhY2tfdGltZVwiOiAxNTU2ODA1%0D%0ANzM0Ljk3NDEyMSwgXCJwcmljaW5nXCI6IDcsIFwiZGV2aWNlX3R5cGVcIjogXCJp%0D%0AUGhvbmUgWFJcIiwgXCJwcm9tb3Rpb25fdHlwZVwiOiAwLCBcImlzX3Nka1wiOiB0%0D%0AcnVlLCBcImFwcF9uYW1lXCI6IFwiXFx1NjFjMlxcdTc0MDNcXHU1ZTFkXCIsIFwi%0D%0AdXVpZFwiOiBcIlwiLCBcIm9zX3ZlcnNpb25cIjogXCIxMi4xLjJcIiwgXCJjcmVh%0D%0AdGl2ZV9pZFwiOiAxNjMyMzk4OTc4MzYzNDIwLCBcInZlcnNpb25fY29kZVwiOiBc%0D%0AIjcuMS4zXCIsIFwidXVpZF9tZDVcIjogXCJcIiwgXCJ3aWR0aFwiOiA2MDAsIFwi%0D%0AdGVtcGxhdGVfcmF0ZVwiOiAwLCBcImltZ19nZW5fdHlwZVwiOiAwLCBcImNsaWVu%0D%0AdF9pcFwiOiBcIjEyMC40Mi4yMTUuMTEzXCIsIFwiYWRfaWRcIjogMTYzMjM5ODQ5%0D%0ANDQ5NTc5NiwgXCJjb252ZXJ0X2lkXCI6IDE2MzIxMzAyNDMyMzU4NDMsIFwiaW1n%0D%0AX21kNVwiOiBcIlwiLCBcImFkX3ByaWNlXCI6IFwiWE1nYW1RQUdQLWxjeUJxWkFB%0D%0AWV82U01LSUtfaEVYamROUkJSVWdcIiwgXCJhcHBfaWRcIjogXCI1MDEyNzA3XCIs%0D%0AIFwic291cmNlX3R5cGVcIjogMSwgXCJtYWNcIjogXCIwMjowMDowMDowMDowMDow%0D%0AMFwiLCBcImlkZmFcIjogXCI4NTcxMzEwQS0wNkRELTQ2Q0UtQkE2OS0zMTk2RTg2%0D%0AODBGRjZcIiwgXCJ1Z19jcmVhdGl2ZV9pZFwiOiBcIlwiLCBcImRldmljZV9pZFwi%0D%0AOiA4NjEyNzg5MTA3MzcyODk2LCBcImxhbmd1YWdlXCI6IFwiZ29sYW5nXCIsIFwi%0D%0AY2lkXCI6IDE2MzIzOTg5NzgzNjM0MjAsIFwidXRcIjogMTQsIFwiaW50ZXJhY3Rp%0D%0Ab25fdHlwZVwiOiAzLCBcIm9wZW5fdWRpZFwiOiBcIlwiLCBcInBvc1wiOiAzLCBc%0D%0AInJlcV9pZFwiOiBcIjlBMTE0QTNELUZBQzctNDlDOC04NUZELUE2MDJGQjRGODA3%0D%0AOXU2NzIwXCIsIFwiaXNfZHNwX2FkXCI6IGZhbHNlLCBcImFkX3Nsb3RfdHlwZVwi%0D%0AOiA1LCBcIm9zX3R5cGVcIjogbnVsbCwgXCJvc1wiOiBcImlvc1wiLCBcInRlbXBs%0D%0AYXRlX2lkXCI6IDB9Iiwic2NvcmUiOjAsIkFkVGl0bGUiOiLkuIDliJnmlZnogrIi%0D%0ALCJidXR0b25UZXh0Ijoi5p%2Bl55yL6K%2Bm5oOFIiwiY291bnREb3duIjowLCJpbWFn%0D%0AZU1vZGUiOjIsImluQXBwIjp0cnVlLCJBZERlc2NyaXB0aW9uIjoi5LiK54%2Bt5peP%0D%0A5Lus6YO95auM5bel6LWE5L2O77yM5p2l55yL55yL6L%2BZ5Liq5Yqp5Yqb5pa55qGI%0D%0AIiwiZXhwaXJlVGltZXN0YW1wIjowLCJjb21tZW50TnVtIjowLCJBZElEIjoiMTYz%0D%0AMjM5ODk3ODM2MzQyMCIsImRlYnVnRGVzY3JpcHRpb24iOiI8QlVNYXRlcmlhbE1l%0D%0AdGE6IDB4MjgxYzRmOWMwPiIsInBob25lIjoiIiwic2hvd191cmxzIjpbXSwiaW1h%0D%0AZ2VBcnkiOlt7IndpZHRoIjo0NTYsImltYWdlVVJMIjoiaHR0cDpcL1wvc2YxLXR0%0D%0AY2RuLXRvcy5wc3RhdHAuY29tXC9pbWdcL3dlYi5idXNpbmVzcy5pbWFnZVwvMjAx%0D%0AOTAyMjY1ZDBkMTlkMmY1YjdkYzYyNGQ1NWE2OGJ%2BY3NfNDU2eDMwMF9xODAuanBl%0D%0AZyIsImhlaWdodCI6MzAwfV0sImhhc2giOjEwNzY3MTA0NDQ4LCJwcmVsb2FkZXJU%0D%0AaW1lc3RhbXAiOjAsImZpbHRlcldvcmRzIjpbeyJkaXNsaWtlSUQiOiI0OjIiLCJo%0D%0AYXNoIjoxMDc3NTk1OTMyOCwic3VwZXJjbGFzcyI6Ik5TT2JqZWN0IiwiZGVidWdE%0D%0AZXNjcmlwdGlvbiI6IjxCVURpc2xpa2VXb3JkczogMHgyODI0YzE3MjA%2BIiwiZGVz%0D%0AY3JpcHRpb24iOiI8QlVEaXNsaWtlV29yZHM6IDB4MjgyNGMxNzIwPiIsIm5hbWUi%0D%0AOiLnnIvov4fkuoYiLCJpc1NlbGVjdGVkIjpmYWxzZX1dLCJpbnRlcmFjdGlvblR5%0D%0AcGUiOjMsInNjcmVlbnNob3QiOmZhbHNlLCJ1bmlvblNwZWNpYWwiOjIsImRlc2Ny%0D%0AaXB0aW9uIjoiPEJVTWF0ZXJpYWxNZXRhOiAweDI4MWM0ZjljMD4ifQ%3D%3D&origin=jrtt"
    uri = "/dsp/v1?data=eyJzb3VyY2UiOiLovabmtarnvZEtLeWugeazoui9puWxlSIsInRhcmdldFVSTCI6%0D%0AImh0dHBzOlwvXC9jaGVsYW5nYXV0by5jb21cL2NoZXpoYW5cL25iXC8xNFwvYmZv%0D%0Acm1cL2pydHQxP3N0eWxlX2lkPTMmYWRfaWQ9MTYzMjI0OTY5NzQ3NjYxNSZfdG91%0D%0AdGlhb19wYXJhbXM9JTdCJTIyY2lkJTIyJTNBMTYzMjI0OTkzNjkzNTk2MyUyQyUy%0D%0AMmRldmljZV9pZCUyMiUzQTM0NjMxNzM3MTcxJTJDJTIybG9nX2V4dHJhJTIyJTNB%0D%0AJTIyJTdCJTVDJTIyYWRfcHJpY2UlNUMlMjIlM0ElNUMlMjJYTXVZbUFBTlYtUmN5%0D%0ANWlZQUExWDVMengtU2s1VHJiZklmQ1J1dyU1QyUyMiUyQyU1QyUyMmNvbnZlcnRf%0D%0AaWQlNUMlMjIlM0ExNjMyMjE5NjQyNTQ5MjU2JTJDJTVDJTIyb3JpdCU1QyUyMiUz%0D%0AQTkwMDAwMDAwMCUyQyU1QyUyMnJlcV9pZCU1QyUyMiUzQSU1QyUyMjE0OEJENzI2%0D%0ALTc1OTMtNEZGQy1CQTFCLTBGNTE4Q0FCQUQ4MnU5NDA5JTVDJTIyJTJDJTVDJTIy%0D%0Acml0JTVDJTIyJTNBOTEyNzA3NTg5JTdEJTIyJTJDJTIyb3JpdCUyMiUzQTkwMDAw%0D%0AMDAwMCUyQyUyMnJlcV9pZCUyMiUzQSUyMjE0OEJENzI2LTc1OTMtNEZGQy1CQTFC%0D%0ALTBGNTE4Q0FCQUQ4MnU5NDA5JTIyJTJDJTIycml0JTIyJTNBOTEyNzA3NTg5JTJD%0D%0AJTIyc2lnbiUyMiUzQSUyMkQ0MUQ4Q0Q5OEYwMEIyMDRFOTgwMDk5OEVDRjg0MjdF%0D%0AJTIyJTJDJTIydWlkJTIyJTNBNTIxODk3NzE5MTUlMkMlMjJ1dCUyMiUzQTEyJTdE%0D%0AJmFwcGVuZD0lN0IlMjJvcGVudXJsJTIyJTNBJTIyJTIyJTJDJTIycG9zdGRhdGEl%0D%0AMjIlM0ElNUIlN0IlMjJfX3R5cGVfXyUyMiUzQSUyMnJlcV9pZCUyMiUyQyUyMmNp%0D%0AZCUyMiUzQTE2MzIyNDk5MzY5MzU5NjMlMkMlMjJyZXFfaWQlMjIlM0ElMjIxNDhC%0D%0ARDcyNi03NTkzLTRGRkMtQkExQi0wRjUxOENBQkFEODJ1OTQwOSUyMiUyQyUyMnJp%0D%0AdCUyMiUzQTkxMjcwNzU4OSU3RCU1RCU3RCIsImNsaWNrX3VybHMiOltdLCJhcHBT%0D%0AaXplIjowLCJzdXBlcmNsYXNzIjoiTlNPYmplY3QiLCJpY29uIjp7IndpZHRoIjoz%0D%0AMDAsImltYWdlVVJMIjoiaHR0cDpcL1wvc2YxLXR0Y2RuLXRvcy5wc3RhdHAuY29t%0D%0AXC9pbWdcL3dlYi5idXNpbmVzcy5pbWFnZVwvMjAxOTA0MjU1ZDBkMDI0NTg0Njhl%0D%0ANzE2NDE3MDg4ODh%2BY3NfMzAweDMwMF9xODAuanBlZyIsImhlaWdodCI6MzAwfSwi%0D%0AZXh0SW5mbyI6IntcInVpZFwiOiA1MjE4OTc3MTkxNSwgXCJ2aWRcIjogXCI2NjUx%0D%0ANzEsNzczODUwLDg1Mjc1OCw4NTc0NTAsODA1MjMwLDc3MDQ5Niw3NzM4OTgsNzc0%0D%0AMTEwLDY1NzI4Myw4NDIwOTksODY4Njg4LDg2ODA1NSw4NjU2OTYsNzgyMzI5LDg2%0D%0ANzQ4Niw4Njc1NDgsODM5OTU1LDc3Mzg2MCw4Njk3NzUsODM2NzAyLDg2OTg0OCw4%0D%0ANjg2OTAsODUwNzE1LDg2ODkyMSw4NTg1NjIsNzc0MTE2LDc3Nzg3Niw4NTYzNDUs%0D%0AODM2NzMxXCIsIFwicml0XCI6IDkxMjcwNzU4OSwgXCJvcml0XCI6IDkwMDAwMDAw%0D%0AMCwgXCJhZF90eXBlXCI6IDEsIFwiaGVpZ2h0XCI6IDE1MCwgXCJwYWNrX3RpbWVc%0D%0AIjogMTU1NzA1NDMyMy43ODMyNywgXCJwcmljaW5nXCI6IDksIFwiZGV2aWNlX3R5%0D%0AcGVcIjogXCJpUGhvbmUgN1wiLCBcInByb21vdGlvbl90eXBlXCI6IDAsIFwiaXNf%0D%0Ac2RrXCI6IHRydWUsIFwiYXBwX25hbWVcIjogXCJcXHU2MWMyXFx1NzQwM1xcdTVl%0D%0AMWRcIiwgXCJ1dWlkXCI6IFwiXCIsIFwib3NfdmVyc2lvblwiOiBcIjExLjQuMVwi%0D%0ALCBcImNyZWF0aXZlX2lkXCI6IDE2MzIyNDk5MzY5MzU5NjMsIFwidmVyc2lvbl9j%0D%0Ab2RlXCI6IFwiNy4xLjNcIiwgXCJ1dWlkX21kNVwiOiBcIlwiLCBcIndpZHRoXCI6%0D%0AIDYwMCwgXCJ0ZW1wbGF0ZV9yYXRlXCI6IDAsIFwiaW1nX2dlbl90eXBlXCI6IDAs%0D%0AIFwiY2xpZW50X2lwXCI6IFwiMjExLjEzOC4xMTcuNjZcIiwgXCJhZF9pZFwiOiAx%0D%0ANjMyMjQ5Njk3NDc2NjE1LCBcImNvbnZlcnRfaWRcIjogMTYzMjIxOTY0MjU0OTI1%0D%0ANiwgXCJpbWdfbWQ1XCI6IFwiXCIsIFwiYWRfcHJpY2VcIjogXCJYTXVZbUFBTlYt%0D%0AUmN5NWlZQUExWDVMengtU2s1VHJiZklmQ1J1d1wiLCBcImFwcF9pZFwiOiBcIjUw%0D%0AMTI3MDdcIiwgXCJzb3VyY2VfdHlwZVwiOiAxLCBcIm1hY1wiOiBcIjAyOjAwOjAw%0D%0AOjAwOjAwOjAwXCIsIFwiaWRmYVwiOiBcIjk4MUZFOTQwLUJENzgtNDQ4QS05MTQ3%0D%0ALUEzREU5Qjk4NEFFRlwiLCBcInVnX2NyZWF0aXZlX2lkXCI6IFwiXCIsIFwiZGV2%0D%0AaWNlX2lkXCI6IDM0NjMxNzM3MTcxLCBcImxhbmd1YWdlXCI6IFwiZ29sYW5nXCIs%0D%0AIFwiY2lkXCI6IDE2MzIyNDk5MzY5MzU5NjMsIFwidXRcIjogMTIsIFwiaW50ZXJh%0D%0AY3Rpb25fdHlwZVwiOiAzLCBcIm9wZW5fdWRpZFwiOiBcIlwiLCBcInBvc1wiOiAz%0D%0ALCBcInJlcV9pZFwiOiBcIjE0OEJENzI2LTc1OTMtNEZGQy1CQTFCLTBGNTE4Q0FC%0D%0AQUQ4MnU5NDA5XCIsIFwiaXNfZHNwX2FkXCI6IGZhbHNlLCBcImFkX3Nsb3RfdHlw%0D%0AZVwiOiA1LCBcIm9zX3R5cGVcIjogbnVsbCwgXCJvc1wiOiBcImlvc1wiLCBcInRl%0D%0AbXBsYXRlX2lkXCI6IDB9Iiwic2NvcmUiOjAsIkFkVGl0bGUiOiLovabmtarnvZEt%0D%0ALeWugeazoui9puWxlSIsImJ1dHRvblRleHQiOiLmn6XnnIvor6bmg4UiLCJjb3Vu%0D%0AdERvd24iOjAsImltYWdlTW9kZSI6MiwiaW5BcHAiOnRydWUsIkFkRGVzY3JpcHRp%0D%0Ab24iOiLnjrDlnKjkuI3kubDlkI7mgpTkuIDlubTvvIE15pyIMTgtMTnml6XlroHm%0D%0As6LovablsZXvvIzmiYDmnInovablnovljYrku7fotbciLCJleHBpcmVUaW1lc3Rh%0D%0AbXAiOjAsImNvbW1lbnROdW0iOjAsIkFkSUQiOiIxNjMyMjQ5OTM2OTM1OTYzIiwi%0D%0AZGVidWdEZXNjcmlwdGlvbiI6IjxCVU1hdGVyaWFsTWV0YTogMHgxYzAzYTMxZTA%2B%0D%0AIiwicGhvbmUiOiIiLCJzaG93X3VybHMiOltdLCJpbWFnZUFyeSI6W3sid2lkdGgi%0D%0AOjQ1NiwiaW1hZ2VVUkwiOiJodHRwOlwvXC9zZjEtdHRjZG4tdG9zLnBzdGF0cC5j%0D%0Ab21cL2ltZ1wvd2ViLmJ1c2luZXNzLmltYWdlXC8yMDE5MDQyNTVkMGQwMjQ1ODQ2%0D%0AOGU3MTY0MTcwODg4OH5jc180NTZ4MzAwX3E4MC5qcGVnIiwiaGVpZ2h0IjozMDB9%0D%0AXSwiaGFzaCI6NzUyMDAwNjYyNCwicHJlbG9hZGVyVGltZXN0YW1wIjowLCJmaWx0%0D%0AZXJXb3JkcyI6W3siZGlzbGlrZUlEIjoiNDoyIiwiaGFzaCI6NzUyNDg0MzQ4OCwi%0D%0Ac3VwZXJjbGFzcyI6Ik5TT2JqZWN0IiwiZGVidWdEZXNjcmlwdGlvbiI6IjxCVURp%0D%0Ac2xpa2VXb3JkczogMHgxYzA4M2ZmZTA%2BIiwiZGVzY3JpcHRpb24iOiI8QlVEaXNs%0D%0AaWtlV29yZHM6IDB4MWMwODNmZmUwPiIsIm5hbWUiOiLnnIvov4fkuoYiLCJpc1Nl%0D%0AbGVjdGVkIjpmYWxzZX1dLCJpbnRlcmFjdGlvblR5cGUiOjMsInNjcmVlbnNob3Qi%0D%0AOmZhbHNlLCJ1bmlvblNwZWNpYWwiOjIsImRlc2NyaXB0aW9uIjoiPEJVTWF0ZXJp%0D%0AYWxNZXRhOiAweDFjMDNhMzFlMD4ifQ%3D%3D&origin=jrtt"
    val uriMap = Uri(uri).query.toMap
    val data = uriMap.get("data")
    println(data)
  }
}