Consider the following knowledge when handle spring websocket websocket upgrade.
1. To hanlde `getPhase()` method collapse error, you need update your class to not implement WebSocketMessageBrokerConfigurer.
   The error looks like `getPhase() in 'org.springframework.messaging.simp.config.AbstractMessageBrokerConfiguration' clashes with getPhase() in 'org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer'; attempting to use incompatible return type `
   Or looks like `getPhase() in org.springframework.messaging.simp.config.AbstractMessageBrokerConfiguration cannot implement getPhase() in org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer`.

   To fix this issue, you need to update your class as below:
   - before  `class WebSocketConfig extends WebSocketMessageBrokerConfigurationSupport implements WebSocketMessageBrokerConfigurer`
   - after `class WebSocketConfig extends WebSocketMessageBrokerConfigurationSupport`
   - override `getPhase()` method in your class is not correct.
   
