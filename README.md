# 탄소 제로

우리가 평소 사용하는 제품들의 탄소 배출량을 조회할 수 있어 자신이 사용하는 제품들이 환경에 대해서 영향을 미치는지 확인할 수 있으며
또한 대체할 수 있는 친환경 제품들을 추천하여 환경보호에 도움이 되는 방향으로 제품을 구매 및 대체할 수 있도록 유도하는 애플리케이션 입니다.

<p align='center'>
    <img src="https://img.shields.io/badge/spring boot-v2.3.5-green?logo=SpringBoot"/>
    <img src="https://img.shields.io/badge/java-v11-ab?logo=Java"/>
    <img src="https://img.shields.io/badge/MariaDB-v10.6.4-ff69b4?logo=MariaDB"/>
    <img src="https://img.shields.io/badge/github actions-v1-abcdef?logo=GitHub Actions"/>
</p>

### 문서

![Screen Shot 2021-08-11 at 5 28 35 PM](https://user-images.githubusercontent.com/14002238/128996327-fcb5e218-ada7-4de5-b25f-d1907a0787c1.png)

서버를 실행하고 아래 주소로 접속하면 위와 같은 화면에서 API 문서 및 테스트를 해볼 수 있습니다.

- http://localhost:8080/swagger-ui/

### 환경 설정

![Screen Shot 2021-08-11 at 5 29 59 PM](https://user-images.githubusercontent.com/14002238/128996500-100028c9-42a3-4714-a866-545f2c466a4f.png)

- `application.yaml` 파일의 `active` 설정에 따라서 서버 환경 설정을 변경할 수 있습니다.

- `prod` 환경으로 접속하시면 실제 `rds`에서 운영하고 있는 데이터베이스가 연결됩니다.

- `h2` 데이터베이스를 사용하여 애플리케이션을 실행해보려면, `test` 환경으로 접속하시면 됩니다.

- `dev` 환경에서는 로컬에 실행중인 데이터베이스를 접속할 수 있습니다.

```
docker run -it -e MYSQL_USER=mysql -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=dev -p 3306:3306 mariadb:10.3.8
```

### 도커 컴포즈

- `application.yaml` 파일의 `profile` 항목의 `active`를 `test`로 설정하고 나서 `docker-compose build`를 터미널에 입력한다.

- 빌드가 완료되고 나서, `docker-compose up`을 하면 로컬에서 서버를 띄울 수 있다.

- 종료할 때는 `docker-compose down`을 입력한다.

### 기여

기여는 언제든지 환영합니다.

| 커밋 타입 | 내용 |
|:--------|:-----|
|    FEAT     |  새로운 기능 추가    |
|     FIX    |   기능 수정 및 버그 수정   |
|     DOCS    |   문서와 관련된 작업   |
|    CLEAN UP |   코드 포맷 및, 세미 콜론 누락, 코드 변경이 없는 경우   |
|     REFACTOR    |   코드 리펙토링을 한 경우   |
|     TEST    |   테스트 코드   |
|     CHORE    |   빌드 관련 작업 및 패키지 매니저   |
