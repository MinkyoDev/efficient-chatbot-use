# 효율적으로 챗봇 사용해보기(Efficient Chatbot Use)

신한 DS 금융 SW 아카데미 JDBC Project

## 프로젝트 소개

상용 LLM모델을 이용한 서비스 제작 시 효율적인 운영을 위한 백엔드 기능 개발

### 프로젝트 목적

#### 1) Chatbot memory

언어모델 자체에는 이전 대화내용을 기억하는 memory기능이 존재하지 않는다. 하지만 챗봇과의 원활한 대화를 위하여 DB에 로그를 저장하고 챗봇에 입력함으로써 memory기능을 구현해 본다.
  
#### 2) Reducing tokens usage

언어모델의 입력과 출력은 token단위로 이루어진다. Prompt token이 많을수록 모델이 분석해야하는 데이터가 많아지며 complition token이 많아질 수록 답변의 길이가 길어진다. 즉 token의 양이 많아질수록 생성 속도는 늦어지고 비용이 늘어가게 된다. 따라서 서비스의 퀄리티가 유지되는 한에서 최대한 token 사용을 줄일 필요가 있다.  

- Cache 탐색
     
    사용자의 동일한 질문에 대하여 답변을 새로 생성하지 않고 log에서 조회하여 바로 답변하도록 구현.
     
- Summary 제작
  
    memory기능을 위하여 prompt에 이전 대화내용을 추가하다보면 굉장히 많은 token이 쌓이게 되므로 일정 token을 넘겼을 시 대화를 요약하여 데이터 량을 줄일 수 있도록 구현.

## User Flow
<img src="https://github.com/MinkyoDev/efficient-chatbot-use/assets/141614581/3a448b36-40ed-45be-b60d-2a713ee87986" width="70%">

## 주요 기능

### 1. Chatbot memory

- #### Memory off
```
채팅을 종료하시려면 q또는 quit을 입력해 주세요.
user> 앞으로 말 끝에 냥을 붙여서 대답해
GPT> 알겠어냥! 질문이 있으면 언제든지 물어보라냥!
user> 안녕?
GPT> 안녕하세요! 무엇을 도와드릴까요?
```

```
Request01
Request: 앞으로 말 끝에 냥을 붙여서 대답해
Response: 알겠어냥! 질문이 있으면 언제든지 물어보라냥!

Request02
Request: 안녕?
Response: 안녕하세요! 무엇을 도와드릴까요?
```

- #### Memory on
```
채팅을 종료하시려면 q또는 quit을 입력해 주세요.
user> 앞으로 말 끝에 냥을 붙여서 대답해
GPT> 알겠어냥! 무엇을 도와드릴까냥?
user> 안녕?
GPT> 안녕하냥! 오늘을 기분이 어떠냥?
```

```
Request01
Request: 앞으로 말 끝에 냥을 붙여서 대답해
Response: 알겠어냥! 무엇을 도와드릴까냥?

Request02
Request: user> 앞으로 말 끝에 냥을 붙여서 대답해
         GPT> 알겠어냥! 무엇을 도와드릴까냥?
         user> 안녕?
Response: 알겠어냥! 무엇을 도와드릴까냥?
```
memory라고 해서 언어모델 자체에서 이전 내용을 기억하지는 못한다. 따라서 사용자의 입력에 이전 내용을 추가함으로써 챗봇이 마치 기억하는 것처럼 답변하도록 한다.

### 2-1. Reducing tokens usage - Cache 탐색

- #### Cache off

```
채팅을 종료하시려면 q또는 quit을 입력해 주세요.
user> 점심 뭐먹으면 좋을까? 하나만 추천해줘.
GPT> 점심으로는 닭가슴살 샐러드를 추천해드립니다. 영양이균형 잡히고, 가볍지만 포만감를 줄 수 있어서 일상에서 활력을 유지하는 데 도움이 됩니다.
user> 점심 뭐먹으면 좋을까? 하나만 추천해줘
GPT> 점심으로 샐러드를 추천합니다. 건강에 좋고 가벼워서 오후 업무에 지장을 주지 않으면서도 다양한 재료를 활용해 영양을 골고루 섭취할 수 있습니다.

total excution time: 10.32s
total token: 220
``` 

- #### Cache on
```
채팅을 종료하시려면 q또는 quit을 입력해 주세요.
user> 점심 뭐먹으면 좋을까? 하나만 추천해줘.
GPT> 비빔밥을 추천 드립니다! 건강에 좋은 재료들을 다양하게 섭취할 수 있어요. 
user> 점심 뭐먹으면 좋을까? 하나만 추천해줘
GPT> 비빔밥을 추천 드립니다! 건강에 좋은 재료들을 다양하게 섭취할 수 있어요.

total excution time: 4.10s
total token: 74
```
답변을 생성하기 전 log에서 같은 내용의 질문이 있는지 확인하는 작업이다. 물론 대화의 문맥에 따라 같은 질문이어도 다른 답변이 나와야 하는 경우도 있지만 간단한 챗봇의 경우 해당 기능을 이용한다면 불필요한 생성 시간과 token을 사용하지 않아도 된다.

### 2-2. Reducing tokens usage - Summary 제작

- #### Original

```
user> 빛의 삼원색에 관하여 알려줘
GPT> 빛의 삼원색은 빨강, 초록, 파랑을 말함니다. 이 세 가지색을 혼합하면 다양한 다른색을 만들어낼 수 있습니다. 이러한 혼합 과정을 더 자세히 알고 싶으신가요?
user> 색의 삼원색과의 차이가 뭐야?
GPT> 색의 삼원색은 물감이나 프린터 잉크등을 혼함하여 다양한색을 만들어내는 기본적인 색상 모델을 말합니다. 빛의 삼원색은 빨강, 초록, 파랑을 조합하여 다양한색을 만들어내는 것을 말합니다. 물감이나 잉크는 빛을 흡수하는 반면, 빛의 삼원색은 빛을 반사하거나 흡수하여 다양한 색을 만들어내는데 사용됩니다.
user> 좀더 자세히 알려줘
GPT> 물감이나 프린터 잉크를 사용하는 색의 삼원색은 시안, 마젠타, 엘로우로 구성되어 있습니다. 시안은 청록색에 가까운 파란색, 마젠타는 붉은 보라색에 가까운 분홈색, 엘로우는 노란색에 가까운 주황색을 나타냅니다. 이 세 가지 색을 조합하여 다양한색상을 만들어내는 것이 가능합니다. 빛의 삼원색과 다르게 색의 삼원색은 빛을 흡수하는 방식으로 색을 혼합하여 색을 만들어 내는 것이 특징입니다.

total token: 573
```

- #### Summary

```
빛의 삼원색은 빨강, 초록, 파랑이며, 이들을 혼합하여 다앙한 색을 만듭니다. 색의 삼원색은 시안, 마젠타, 옐로우이며, 이들은 물감이나 잉크 형태로 색을 흡수하는 방식으로 색을 혼합합니다. 두 시스템의 주요 차이는 빛의 삼원색이 빛을 반사하거나 흡수하여 색을 생성하는 반면, 색의 삼원색은 물질이 빛을 흡수하여 색을 혼합하는 방식에 있습니다.

total token: 201
```
prompt에 들어가는 log의 total token의 양이 일정 갯수를 넘어가면 summary를 생성한다. limit의 크기와 얼마나 요약할지는 상황에 따라 다르지만 많은 양의 토큰을 아낄 수 있음을 알 수 있다.

## 추가 기능

### 1. Generate Chat name

<table>
        <tbody>
            <tr>
                <td align="center"><b>Chat</b></td>
                <td align="center"><img src="https://github.com/MinkyoDev/efficient-chatbot-use/assets/141614581/6ea8a136-aa04-4b79-b894-e539b52142d9" width="500"></td>
                <td align="center"><img src="https://github.com/MinkyoDev/efficient-chatbot-use/assets/141614581/2b1d7407-5754-4151-958e-8d263ee9a98f" width="500"></td>
            </tr>
            <tr>
                <td align="center"><b>name</b></td>
                <td align="center">null</td>
                <td align="center">"Request for Lunch Recommendation and Curry Recipe Instructions"</td>
            </tr>
        </tbody>
    </table>

대화 종료시 GPT를 이용하여 자동으로 대화 내용에서 제목을 만들어 채팅방 이름으로 사용.

### 2. Stream

<table>
        <tbody>
            <tr>
                <td align="center"><img src="https://github.com/MinkyoDev/efficient-chatbot-use/assets/141614581/91dbffdd-dcfc-48d2-9901-7aa8002e5d3d" width="500"></td>
                <td align="center"><img src="https://github.com/MinkyoDev/efficient-chatbot-use/assets/141614581/7d4dd992-5e4f-445d-886d-2b9de9df9a6b" width="500"></td>
            </tr>
            <tr>
                <td align="center">Stream off</td>
                <td align="center">Stream on</td>
            </tr>
        </tbody>
    </table>

## Service Logic
<img src="https://github.com/MinkyoDev/efficient-chatbot-use/assets/141614581/9ef4113d-61de-4613-8fd5-82767e83e0c7" width="70%">

## ERD
<img src="https://github.com/MinkyoDev/efficient-chatbot-use/assets/141614581/848e77b6-0a2c-43d0-b0c5-f9d9cd9982c5" width="70%">

