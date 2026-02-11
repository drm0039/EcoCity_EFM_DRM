# EcoCity - Gestión de Incidencias Urbanas

**Plataforma de Ciencia Ciudadana para el reporte y gestión de incidencias en la vía pública.**

**EcoCity** es una aplicación nativa para Android desarrollada en Java. Su objetivo es empoderar a los ciudadanos para que reporten problemas en su entorno (farolas rotas, desbordamientos de ríos, acumulación de basura, mobiliario dañado) mediante fotografías geolocalizadas, facilitando una respuesta rápida y eficiente por parte de las autoridades.

---

## Características Principales

* **Reporte Visual:** Captura de fotos directamente desde la app para documentar incidencias urbanas.
* **Asistente IA Avanzado:** Chatbot integrado con **Google Gemini 2.5 Flash** que actúa como operador de soporte inteligente.
* **Protocolo de Seguridad:** La IA analiza el contexto y advierte al usuario en situaciones de riesgo (fuego, cables eléctricos, inundaciones) para que llame al 112.
* **Geolocalización:** Registro preciso de la ubicación de cada incidencia reportada.
* **Interfaz Nativa:** Comunicación fluida sin depender de librerías externas pesadas.

---

## Inteligencia Artificial: El Cerebro de EcoCity

El asistente virtual (`ChatActivity.java`) no es un chatbot genérico. Está altamente personalizado mediante **Ingeniería de Prompts** para cumplir una función de servicio público.

### 1. Especificaciones Técnicas
* **Modelo:** `gemini-2.5-flash` (Optimizado para velocidad y precisión en dispositivos móviles).
* **API:** Google Generative Language API (`v1beta`).
* **Integración:** Implementación pura con `HttpURLConnection` y parsing de JSON nativo para máximo rendimiento.

### 2. Personalidad del Sistema (System Prompt)
Hemos inyectado una personalidad específica a la IA que se envía oculta en cada petición ("System Prompt"). Sus reglas de comportamiento son:

* **Rol:** Asistente de Soporte Ciudadano y Medioambiental.
* **Seguridad:** Prioridad absoluta. Si el usuario menciona peligro inminente, la IA instruye alejarse y contactar a Emergencias.
* **Filtro de Contenido:** Se mantiene enfocado estrictamente en temas de la app, rechaza amablemente conversaciones sobre política, deportes o temas personales.
* **Tono:** Profesional, conciso y orientado a la acción en pantalla móvil.

---

## Instalación y Configuración

Sigue estos pasos para ejecutar el proyecto en tu entorno local (Android Studio):

### Paso 1: Clonar el Repositorio
```bash
git clone [https://github.com/TU_USUARIO/EcoCity_EFM_DRM.git](https://github.com/TU_USUARIO/EcoCity_EFM_DRM.git)
```

---

### 3. Configuración de la API de Gemini

** En la clase ChatActivity.java

//Aqui se pone la API
private static final String API_KEY = "AIzaSy..."; 

// Aqui se configura el modelo Gemini 2.5 Flash
private static final String URL_GEMINI = "[https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=](https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=)";

---

## API

AIzaSyC1zxSx6U5KRuv0nw5Fh1-VEQDnUyWhWyc

---

## Autores

** Nombre: 
--Daniel Rivera Miranda
--Enrique de la Fuente Méndez

**Correo:
--drm0039@alu.medac.es
--efm009@alu.medac.es
