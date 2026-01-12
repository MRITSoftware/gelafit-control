# 🔧 Configurar Gradle Wrapper

O Gradle Wrapper é necessário para o workflow do GitHub Actions funcionar.

## Opção 1: Gerar Automaticamente (Recomendado)

O workflow do GitHub Actions irá gerar automaticamente se não encontrar o wrapper.

## Opção 2: Gerar Localmente

Se você quiser gerar localmente antes de fazer push:

### Windows (PowerShell)
```powershell
# Se você tem Gradle instalado
gradle wrapper --gradle-version 8.2

# Ou usando o Android Studio
# O Android Studio geralmente gera automaticamente ao sincronizar
```

### Linux/Mac
```bash
# Se você tem Gradle instalado
gradle wrapper --gradle-version 8.2

# Ou usando o Android Studio
```

### Via Android Studio
1. Abra o projeto no Android Studio
2. Vá em **File > Settings > Build, Execution, Deployment > Build Tools > Gradle**
3. Selecione **Gradle wrapper**
4. Clique em **Apply** e **OK**
5. O Android Studio irá gerar os arquivos automaticamente

## Arquivos que devem ser commitados

Após gerar, você deve ter estes arquivos:

```
gradlew          (script Unix)
gradlew.bat      (script Windows)
gradle/
  wrapper/
    gradle-wrapper.jar
    gradle-wrapper.properties
```

Todos estes arquivos devem ser commitados no Git.

## Verificar se está funcionando

```bash
# Windows
.\gradlew.bat --version

# Linux/Mac
./gradlew --version
```

Se mostrar a versão do Gradle, está funcionando! ✅
