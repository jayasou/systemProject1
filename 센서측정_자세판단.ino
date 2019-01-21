#define distanceSensor1 A5 // 센서핀 A0핀으로 설정
#define size 5
unsigned long prev = 0;
unsigned int fsrindex = 0;
unsigned int fsrindex1 = 0;
unsigned int fsrindex2 = 0;
unsigned int cmindex = 0;
float Rfsr4, Rfsr0, Rfsr1;
int vol;
float cm; // 거리값 저장변수
float midFsr1[size];
float midFsr2[size];
float midFsr[size];
float midCm[size];

void setup() {
  Serial.begin(115200); // 센서값을 읽기 위해 시리얼 모니터를 사용할 것을 설정.
  pinMode(distanceSensor1, INPUT); // 센서핀 입력설정
}
void fsr1() {
  int FSRpin0 = 0; // FSRpin을 아날로그0(A0)에 연결
  int Vo0; // 센서값을 저장할 변수
  Vo0 = analogRead(FSRpin0); // 아날로그를 입력 받음 (0~1023)
  Rfsr0 = ((9.78 * Vo0) / (1 - (Vo0 / 1023.0)));  
  midFsr1[fsrindex1] = Rfsr0; fsrindex1++;
}

void fsr2() {
  int FSRpin1 = 1; // FSRpin을 아날로그0(A0)에 연결
  int Vo1; // 센서값을 저장할 변수
  Vo1 = analogRead(FSRpin1); // 아날로그를 입력 받음 (0~1023)
  Rfsr1 = ((9.78 * Vo1) / (1 - (Vo1 / 1023.0)));
  midFsr2[fsrindex2] = Rfsr1; fsrindex2++;
}

void fsr5() {   
  int FSRpin4 = 4; // FSRpin을 아날로그0(A0)에 연결
  int Vo4; // 센서값을 저장할 변수  
  Vo4 = analogRead(FSRpin4); // 아날로그를 입력 받음 (0~1023)
  Rfsr4 = ((9.78 * Vo4) / (1 - (Vo4 / 1023.0)));
  midFsr[fsrindex] = Rfsr4; fsrindex++; 
}

void checkDistance() {
 vol = analogRead(distanceSensor1); // 센서의 전압값을 변수에 저장
 cm = (1 / (0.000413153 * vol - 0.0055266887));  // 입력받은 전압값을 거리로 계산
 // 하한값에서 3 cm 출력
  if (vol > 600)     cm = 3;
  
  // 상한값에서 37 cm 출력
 if (vol < 80 )       cm = 37;
 
 midCm[cmindex] = cm; cmindex++;  
}

void chk(float distance, float fsr, float fsr0, float fsr1)
{
  int chkStr; char result;
  //바른 자세로 앉았을 때
  if(distance > 3 && distance <= 15) { //1. 허리를 폈을 때
    if(fsr0 != 0.00 && fsr1 != 0.00) { //2. 양쪽 압력센서값이 0이 아닐 때
      if(fabs(fsr0-fsr1) <= 900) { //3. 차이값이 500이하 일 때 
        if(fsr != 0) { //허리 압력이 0이 아닐 때 => 바른 자세 !!!
          chkStr = 1; result = 'A';
        } else { //허리 압력이 0일 때 => 허리를 뒤로 젖혔을 때
          chkStr = 0; result = 'z';           
        }        
      } else { // 차이값이 500 초과일 때 '다리를 꼰 경우'로 판단
        chkStr = 0; result = 'B';
        if(fsr0 > fsr1)
        {
          result='F';
        }
        else
        {
          result='G';
        }
      }
    } else { // 양쪽 센서값이 0일 때 '드러누운 자세'로 판단
      chkStr = 0; result = 'C';
    }
  } else {//'허리를 굽힌 자세'로 판단   
    chkStr = 0; result = 'D';
  }
  if(distance == 37 && fsr == 0 && fsr0 == 0 && fsr1 == 0) { // '앉지 않은 경우'로 판단
    chkStr = 2; result = 'E';
  }
  Serial.print(chkStr); Serial.print("___"); Serial.println(result);
}


float sort(float arr[]) {
  for(int i=0; i<size-1; i++) {
      int j; int mimIndex; float temp;
    for(j=0; j<size-1; j++) {
      if(arr[j] > arr[j+1]) {
        temp = arr[j+1];
        arr[j+1] = arr[j];
        arr[j]=temp;
      }      
     }
  }
  return arr[size/2];
}

void printArr(float arr[]) {
  Serial.println();
  for(int i=0; i<size; i++) {Serial.print(arr[i]);  Serial.print("___");  }
}

void loop()
{  
  fsr1();
  fsr2();
  fsr5();  
  checkDistance();
  delay(1000);
  
  if(millis() - prev > 5000) {  
    Rfsr0 = sort(midFsr1);
    Rfsr1 = sort(midFsr2);
    Rfsr4 = sort(midFsr);
    cm=sort(midCm);
    chk(cm, Rfsr4, Rfsr0, Rfsr1);    
    prev = millis();

    fsrindex = 0; cmindex = 0;
  }  
}
