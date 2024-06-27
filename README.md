## 命名规则：
groupId:统一使用com.deepinnet
<br/>
artifactId：deepinnet-应用名

包命名规范：com.deepinnet.应用名.模块名
<br/>
举例：com.deepinnet.rainsfall.biz

## 项目分层说明：
- rainsfall-app-starter（SpringBoot应用启动类）
- rainsfall-biz（业务层）
  - rainsfall-biz-service-impl（业务层的实现，具体业务流程编排）

- rainsfall-common（通用层）
  - rainsfall-common-dal（仓储层）
  - rainsfall-common-service
    - rainsfall-common-service-facade（需要向外界暴露的领域服务）
    - rainsfall-common-service-integration（集成的第三方接口）
  - rainsfall-common-util（工具）

- rainsfall-core（领域层）
  - rainsfall-core-service（本系统内使用的service接口）
  - rainsfall-core-model（领域模型）

## 各模块依赖关系：
- biz：负责业务流程编排，依赖于common-service-facade、common-service-integration、
  common-service-util、core-service
- core-service：本系统内的服务， 不需要向外暴露，依赖core-model、
  common-dal、common-util
