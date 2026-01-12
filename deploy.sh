#!/bin/bash
# Script Bash para fazer deploy inicial no GitHub
# Execute: chmod +x deploy.sh && ./deploy.sh

echo "🚀 Iniciando deploy para GitHub..."

# Verificar se Git está instalado
if ! command -v git &> /dev/null; then
    echo "❌ Git não encontrado. Por favor, instale o Git primeiro."
    exit 1
fi

echo "✅ Git encontrado: $(git --version)"

# Verificar se estamos em um repositório Git
if [ ! -d .git ]; then
    echo "📦 Inicializando repositório Git..."
    git init
fi

# Verificar remote
if ! git remote get-url origin &> /dev/null; then
    echo "🔗 Adicionando remote do GitHub..."
    git remote add origin https://github.com/MRITSoftware/boot-receiver.git
else
    echo "✅ Remote já configurado: $(git remote get-url origin)"
fi

# Verificar status
echo ""
echo "📊 Status do repositório:"
git status --short

# Perguntar se deseja continuar
echo ""
read -p "❓ Deseja fazer commit e push? (S/N) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Ss]$ ]]; then
    echo "❌ Operação cancelada."
    exit 0
fi

# Adicionar arquivos
echo ""
echo "📝 Adicionando arquivos..."
git add .

# Fazer commit
echo ""
read -p "💬 Mensagem do commit (ou pressione Enter para usar padrão): " commitMessage
if [ -z "$commitMessage" ]; then
    commitMessage="feat: Adiciona aplicativo Boot Receiver para Android Stick"
fi

echo "💾 Fazendo commit..."
git commit -m "$commitMessage"

# Verificar branch
currentBranch=$(git branch --show-current)
if [ "$currentBranch" = "master" ]; then
    echo "🔄 Renomeando branch de 'master' para 'main'..."
    git branch -M main
    currentBranch="main"
fi

# Fazer push
echo ""
echo "🚀 Fazendo push para GitHub..."
echo "⚠️  Se for a primeira vez, você pode precisar fazer autenticação."

if git push -u origin "$currentBranch"; then
    echo ""
    echo "✅ Deploy concluído com sucesso!"
    echo ""
    echo "📦 Os APKs serão gerados automaticamente pelo GitHub Actions."
    echo "🔗 Acesse: https://github.com/MRITSoftware/boot-receiver/actions"
else
    echo ""
    echo "❌ Erro ao fazer push. Verifique suas credenciais do GitHub."
    exit 1
fi

echo ""
echo "✨ Pronto! O workflow do GitHub Actions irá gerar os APKs automaticamente."
