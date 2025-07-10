echo "🔨 Construyendo imagen Docker desde la raíz del proyecto..."

docker build -t autoslocos:latest -f Dockerfile ..  # Contexto: ".." (la raíz)

echo "🏷️ Etiquetando imagen..."
docker tag autoslocos:latest jantoniio3/autoslocos:latest

echo "📤 Subiendo a Docker Hub..."
docker push jantoniio3/autoslocos:latest
echo "🔄 Actualizando imagen en el servidor..."


echo ""🔄 Actualizando imagen de docker""
docker-compose pull


echo "✅ ¡Todo listo!"