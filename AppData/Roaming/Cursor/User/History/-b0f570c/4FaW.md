# Como Gerar o APK

## Opção 1: Android Studio (Recomendado)

1. **Instale o Android Studio:**
   - Baixe de: https://developer.android.com/studio
   - Instale e abra o Android Studio

2. **Abra o projeto:**
   - File → Open → Selecione a pasta `android_app`
   - Aguarde o Gradle sincronizar

3. **Copie os arquivos para assets:**
   - Copie estes arquivos para `android_app/app/src/main/assets/`:
     - `tuya_server_enhanced.py`
     - `requirements.txt`
     - `start_server.sh`
     - `stop_server.sh`

4. **Gere o APK:**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Ou: Build → Generate Signed Bundle / APK → APK
   - O APK estará em: `android_app/app/build/outputs/apk/debug/app-debug.apk`

5. **Instale no tablet:**
   - Transfira o APK para o tablet
   - Ative "Fontes desconhecidas" nas configurações
   - Instale o APK

## Opção 2: Linha de Comando (Gradle)

**Pré-requisitos:**
- Java JDK 11 ou superior
- Android SDK instalado

**Comandos:**

```bash
cd android_app

# Copie os arquivos para assets primeiro
mkdir -p app/src/main/assets
cp ../tuya_server_enhanced.py app/src/main/assets/
cp ../requirements.txt app/src/main/assets/
cp ../start_server.sh app/src/main/assets/
cp ../stop_server.sh app/src/main/assets/

# Gere o APK
./gradlew assembleDebug

# O APK estará em:
# app/build/outputs/apk/debug/app-debug.apk
```

## Opção 3: Usar um Serviço Online (Mais Rápido)

Se não quiser instalar o Android Studio, você pode usar:

1. **GitHub Actions** (se tiver o código no GitHub)
2. **Appetize.io** ou similar para testar
3. **Buildozer** (alternativa Python para Android)

## Estrutura de Arquivos Necessária

Antes de compilar, certifique-se de que os arquivos estão em:

```
android_app/
├── app/
│   └── src/
│       └── main/
│           └── assets/
│               ├── tuya_server_enhanced.py
│               ├── requirements.txt
│               ├── start_server.sh
│               └── stop_server.sh
```

## Notas Importantes

- **Permissões:** O app precisa de permissão para acessar o Termux
- **Termux:** O tablet precisa ter o Termux instalado
- **Root:** Não é necessário root, mas algumas operações podem precisar de permissões especiais

## Alternativa: Script de Build Automatizado

Criei também um script `build_apk.sh` que automatiza o processo!

