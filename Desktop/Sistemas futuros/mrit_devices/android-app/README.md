# Aplicativo Android de Modo Quiosque

Este aplicativo Android é responsável por gerenciar tablets em modo quiosque, permitindo controle remoto e execução de aplicativos específicos.

## Funcionalidades

- Início automático após boot do dispositivo
- Modo quiosque com bloqueio de sistema
- Execução de aplicativos específicos
- Recebimento de comandos remotos
- Proteção contra desinstalação

## Requisitos

- Android 6.0 (API 23) ou superior
- Permissões de Device Owner
- Conexão com internet

## Instalação

1. Compile o aplicativo usando Android Studio
2. Instale o APK no tablet
3. Configure como Device Owner usando o comando ADB:

```bash
adb shell dpm set-device-owner com.mrit.devices/.receivers.AdminReceiver
```

## Configuração

1. Após a instalação, o aplicativo solicitará permissões de administrador
2. Aceite as permissões para ativar o modo quiosque
3. O aplicativo iniciará automaticamente após o boot do dispositivo

## Segurança

- Proteção contra desinstalação
- Bloqueio de botões e barra de status
- Restrições de sistema
- Autenticação de comandos remotos

## Desenvolvimento

Para desenvolver ou modificar o aplicativo:

1. Clone o repositório
2. Abra o projeto no Android Studio
3. Configure as variáveis de ambiente necessárias
4. Compile e instale no dispositivo

## Licença

Este projeto é proprietário e confidencial. 