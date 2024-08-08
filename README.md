## 프로젝트 소개

절약왕국 프로젝트는 MSA와 쿠버네티스를 기반으로 구축한 디지털 플랫폼으로, 최저가 공유와 거지방 커뮤니티를 통합하여 경제적이고 환경 친화적인 소비를 촉진하는 것을 목표로 한다. MSA를 통해 각 기능이 독립적으로 개발되고 운영되어 유연성과 확장성이 향상된다. 쿠버네티스를 활용한 컨테이너 오케스트레이션은 높은 가용성을 보장하고, 자동 스케일링 및 롤링 업데이트를 통해 서비스 중단 없이 효율적으로 자원을 관리한다. 이로써 절약왕국은 사용자에게 안정적이고 지속 가능한 서비스를 제공한다.
<br></br>

### 서비스 개발 시 고려한 중점사항
1. 마이크로 서비스 아키텍처 설계 : 단일 장애 지점을 최소화하여 서버 안정성 향상 각 서비스의 필요 리소스에 맞게 독립적으로 스케일링
    <details>
    <summary>더보기</summary>

    <!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
   ![스크린샷 2024-06-12 173706](https://github.com/j-ra1n/MSA_Thrift_Kingdom/assets/118893707/7b4afb4c-9888-4eaf-acf2-b48a07e70a6b)

   
     - Kubernetes 클러스터를 활용하여 애플리케이션을 배포하고 관리한다. 
     - 클러스터는 마스터 노드 1개와 워커 노드 2개로 이루어져 있으며, 이를 통해 애플리케이션의 배포, 확장, 관리, 복구 등의 오케스트레이션을 자동화한다. 
    <br></br>
    
   ![스크린샷 2024-08-07 215423](https://github.com/user-attachments/assets/c15cc759-ff8a-4cf8-98cc-8d22a6471075)![스크린샷 2024-08-07 215450](https://github.com/user-attachments/assets/0236b649-d513-4d35-a4e3-d9badc590bc1)


   
    - 클러스터의 전체 파드 목록을 보여준다. 
    </details>
    <br></br>

3. 데이터 가용성 확보 : 각 서비스별로 적합한 데이터베이스 사용 독립적인 PV(PersistentVolume), PVC(PersistentVolumeClaim) 생성으로 데이터 영구 저장
    <details>
    <summary>더보기</summary>

    <!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
   ![제목 없음](https://github.com/j-ra1n/MSA_Thrift_Kingdom/assets/118893707/0e81e39a-1886-4c31-94c2-f6f22223a9bb)



    - 데이터의 영속성 보장과 유연한 스토리지 할당을 위해 Persistent Volume (PV)과 Persistent Volume Claim (PVC)를 사용한다.
    - 이를 통해 애플리케이션 재시작 시에도 데이터를 안전하게 유지하고, 필요한 스토리지를 동적으로 요청할 수 있다. 

    </details>
    <br></br>
    
4. 자체 복구 기능 활용 : 쿠버네티스의 Self-healing 기능으로 비정상 컨테이너 자동 교체 안정적인 서비스 유지

    <details>
    <summary>더보기</summary>

    <!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
   ![image](https://github.com/j-ra1n/MSA_Thrift_Kingdom/assets/118893707/86bf8ecb-7d0d-4d7a-a379-b053fc268eb0)

    - initialDelaySeconds: 컨테이너가 시작된 후 처음 상태 확인을 시작하기 전 대기 시간을 60초로 설정한다. 
    - periodSeconds: 상태 확인 주기는 30초로 설정한다.  
    - 이 설정을 통해 Kubernetes는 컨테이너가 비정상적인 상태일 때 자동으로 재시작하여 서비스의 가용성과 안정성을 보장한다.

    </details>
     <br></br>
    
5. 무중단 배포 구현 : 쿠버네티스의 Rolling Update 기능 활용 새로운 버전 배포 시 서비스 중단 최소화 

    <details>
    <summary>더보기</summary>

    <!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
   
    ![image](https://github.com/j-ra1n/MSA_Thrift_Kingdom/assets/118893707/19cda09f-9d92-4861-8622-dbfae02505ac)



   
   
    - RollingUpdate: Deployment의 업데이트 전략으로 롤링 업데이트를 사용함을 지정한다.
    - maxUnavailable: 업데이트 과정에서 최대 몇 개의 파드가 동시에 중단될 수 있는지를 지정합니다. 여기서는 1개 파드가 중단될 수 있음을 의미한다. 
    - maxSurge: 업데이트 과정에서 최대 몇 개의 파드를 추가로 생성할 수 있는지를 지정합니다. 여기서는 1개 파드를 추가로 생성할 수 있음을 의미한다. 
    - 이렇게 하면 서비스 중단을 최소화하면서 새로운 버전으로 안전하게 업데이트할 수 있다. 

    </details>
    <br></br>
    
6. 자동 스케일링 적용 : 쿠버네티스의 Auto-Scaling 기능 활용 HPA(HorizontalPodAutoscaler)로 CPU 사용량에 따른 레플리카 셋 개수 조절

    <details>
    <summary>더보기</summary>

    <!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
   ![image](https://github.com/j-ra1n/MSA_Thrift_Kingdom/assets/118893707/ee103a61-88b8-42a1-a9bc-96885ca407d8)



    - Kubernetes 클러스터에서 자동 스케일링을 구현했다. 이 설정을 통해 파드 수는 부하에 따라 자동으로 조정된다.
    - 최소 파드 수는 3개, 최대 파드 수는 50개로 제한되며, CPU 사용률이 평균 50%가 되도록 파드 수를 조정한다.
    - 이를 통해 애플리케이션의 부하에 따라 효율적인 리소스 사용이 보장된다.
    <br></br>
    ![화면 캡처 2024-06-24 180548](https://github.com/rndudals/MSA_Project/assets/102203336/068d04d6-cc8d-427a-ad24-20a41d3bfad1)

    - HPA가 CPU 사용률에 따라 Replicas수를 조정하는 과정 이다.
    - 부하가 증가할 때 Replicas 수가 동적으로 늘어난다.
    </details>
    <br></br
    
7. 성능 모니터링 및 측정 : Jmeter와 Lighthouse를 이용한 반응 속도 및 서비스 성능 측정

    <details>
    <summary>더보기</summary>

    <!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
   ![제목 없음 (1)](https://github.com/j-ra1n/MSA_Thrift_Kingdom/assets/118893707/f66021c5-7e91-4b2e-9f16-cb9cc2e19250)


    - 초당 500명이 동시 접속할 때의 성능 테스트 결과이다. 

    </details>
    <br></br>




## 프로젝트 구조
![1](https://github.com/rndudals/MSA_Project/assets/102203336/b9c01d9c-2061-469c-a398-a8078146407d)

- 총 5개의 서버
1. Front – Server(front-server)

2. 인증 서버 (auth-server)

3. 자유게시판 서버 (Bulletin Board-server : bb-server)

4. 자유게시판 댓글 서버 (Comment-server : cm-server)

5. 최저가 공유 서버 (Sharing Board- server : sb-server)

<br></br>

## Service Mesh
![2](https://github.com/rndudals/MSA_Project/assets/102203336/5a8390f2-6f1b-4edd-a76f-1119ed963d04)

각 서비스가 데이터베이스와 연결되어 있으며, istio-ingressgateway를 통해 외부 트래픽이 내부 서비스로 라우팅된다. 

<br></br>


## 프로젝트 기능

### 1. 비회원/회원 가입 기능
![화면 캡처 2024-06-24 173201](https://github.com/rndudals/MSA_Project/assets/102203336/6380a198-a280-4471-972c-1fbc974a2eca)
<br></br>

### 2. 거지방 커뮤니티 자유게시판(Bulletin Board) 기능
![화면 캡처 2024-06-24 173223](https://github.com/rndudals/MSA_Project/assets/102203336/08220c47-1f9f-460e-a3f2-ff6cb10b8cfc)

<br></br>
### 3. 물품 최저가 공유 기능(Sharing Board) 기능
![image](https://github.com/j-ra1n/MSA_Thrift_Kingdom/assets/118893707/73e66d32-9a08-4e9c-92e8-f55409c907d5)


