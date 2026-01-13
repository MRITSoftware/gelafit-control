# Sistema de Gerenciamento Remoto de Tablets Android

Este projeto consiste em uma solução personalizada para gerenciamento remoto de tablets Android em modo quiosque.

## Estrutura do Projeto

O projeto está dividido em duas partes principais:

### 1. Aplicativo Android (android-app)
- Aplicativo que gerencia os tablets em modo quiosque
- Inicia automaticamente após o boot
- Bloqueia acesso ao sistema operacional
- Gerencia aplicativos em modo travado
- Comunica-se com o servidor central

### 2. Painel Web (web-panel)
- Interface web para gerenciamento dos tablets
- Dashboard com status dos dispositivos
- Configuração de aplicativos por tablet
- Envio de comandos remotos
- Integração com Supabase

## Requisitos

### Android App
- Android 6.0 (API 23) ou superior
- Permissões de Device Owner
- Conexão com internet

### Web Panel
- Node.js 16+
- PostgreSQL/Supabase
- Navegador web moderno

## Configuração

### Android App
1. Instalar o APK no tablet
2. Configurar como Device Owner via ADB:
```bash
adb shell dpm set-device-owner com.seuapp/.AdminReceiver
```

### Web Panel
1. Configurar variáveis de ambiente
2. Instalar dependências
3. Iniciar servidor

## Segurança
- Autenticação JWT
- HTTPS
- Proteção contra desinstalação
- Controle de acesso por usuário

## Licença
Este projeto é proprietário e confidencial. 