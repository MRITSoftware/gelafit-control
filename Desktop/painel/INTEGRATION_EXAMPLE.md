# Como Integrar UserSettings em Todas as Páginas

## 1. Importar o Componente

```tsx
import UserSettings from './UserSettings'
```

## 2. Adicionar Estado

```tsx
const [showUserSettingsModal, setShowUserSettingsModal] = useState(false)
```

## 3. Atualizar handleSettings

```tsx
const handleSettings = () => {
  setShowUserMenu(false)
  setShowUserSettingsModal(true)
}
```

## 4. Adicionar Componente no JSX

```tsx
{/* Modal de Configurações do Usuário */}
<UserSettings 
  isOpen={showUserSettingsModal}
  onClose={() => setShowUserSettingsModal(false)}
/>
```

## Exemplo Completo para Anúncios.tsx

```tsx
// 1. Adicionar import
import UserSettings from './UserSettings'

// 2. Adicionar estado (junto com os outros estados)
const [showUserSettingsModal, setShowUserSettingsModal] = useState(false)

// 3. Atualizar handleSettings (se existir)
const handleSettings = () => {
  setShowUserMenu(false)
  setShowUserSettingsModal(true)
}

// 4. Adicionar no final do JSX, antes do fechamento do componente
<UserSettings 
  isOpen={showUserSettingsModal}
  onClose={() => setShowUserSettingsModal(false)}
/>
```

## Páginas que Precisam da Integração:
- ✅ Displays.tsx (já integrado)
- ⏳ Anuncios.tsx
- ⏳ Playlists.tsx  
- ⏳ Agendamentos.tsx
- ⏳ Dashboard.tsx (Resumo)
