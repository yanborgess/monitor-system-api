import { useEffect, useState } from 'react'
import type { Device } from './types/device'

function App() {
  // Criamos um estado para guardar a lista de dispositivos
  // O <Device[]> diz ao TS que isso é uma lista (array) de dispositivos
  const [devices, setDevices] = useState<Device[]>([])

  // Função para buscar os dados no Java
  const carregarDados = async () => {
    try {
      const resposta = await fetch("http://localhost:8080/api/devices")
      const dados = await resposta.json()
      setDevices(dados) // Guarda os dados na "memória" (state)
    } catch (erro) {
      console.error("Erro ao conectar com o Backend Java:", erro)
    }
  }

  // O useEffect roda a função assim que o componente aparece na tela
  useEffect(() => {
    carregarDados()
  }, [])

  return (
    <div style={{ padding: '20px' }}>
      <h1>Monitoramento de Segurança</h1>
      <div style={{ display: 'flex', gap: '10px' }}>
        {devices.map(dev => (
          <div key={dev.id} style={{ 
            border: '1px solid black', 
            padding: '10px',
            background: dev.active ? '#e0ffe0' : '#ffe0e0' 
          }}>
            <h3>{dev.name}</h3>
            <p>Status: {dev.active ? 'Ativo' : 'Inativo'}</p>
          </div>
        ))}
      </div>
    </div>
  )
}

export default App