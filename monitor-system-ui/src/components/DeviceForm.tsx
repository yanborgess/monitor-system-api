import React, { useState } from 'react';

interface DeviceFormProps {
  onSuccess: () => void;
}

export function DeviceForm({ onSuccess }: DeviceFormProps) {
  const [formData, setFormData] = useState({
    name: '',
    type: '',
    location: '',
    active: true
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/api/devices", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        alert("✅ Dispositivo cadastrado com sucesso!");
        // Limpa o formulário após o sucesso
        setFormData({ name: '', type: '', location: '', active: true });
        onSuccess(); // Esta função vai fazer o App.tsx recarregar a lista
      } else {
        alert("❌ Erro ao salvar o dispositivo.");
      }
    } catch (error) {
      console.error("Erro na requisição POST:", error);
      alert("Servidor offline ou erro de rede.");
    }
  };

  return (
    <div className="admin-form-container">
      <h3>🆕 Cadastrar Novo Dispositivo</h3>
      <form className="device-form" onSubmit={handleSubmit}>
        <input 
          type="text"
          placeholder="Nome (Ex: Câmera Corredor)" 
          value={formData.name}
          onChange={e => setFormData({...formData, name: e.target.value})}
          required 
        />
        <input 
          type="text"
          placeholder="Tipo (Ex: Câmera, Sensor)" 
          value={formData.type}
          onChange={e => setFormData({...formData, type: e.target.value})}
          required 
        />
        <input 
          type="text"
          placeholder="Localização (Ex: Bloco A)" 
          value={formData.location}
          onChange={e => setFormData({...formData, location: e.target.value})}
          required 
        />
        <button type="submit">Adicionar Dispositivo</button>
      </form>
    </div>
  );
}