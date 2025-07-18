package com.WorldTool.DisplaySystem.EditorTools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SimpleCubeRenderer {

        private final OrthographicCamera camera;
        private final Environment environment;
        private final ModelBatch modelBatch;
        private ModelInstance cubeInstance;
        private Model cubeModel;
        private final float scale = 50f;

        private TextureRegion topFace;
        private TextureRegion bottomFace;
        private final TextureRegion[] sideFaces = new TextureRegion[4];

        private final Material[] faceMaterials = new Material[6];

        private boolean built = false;
        private final Vector2 screenOrigin;

        private boolean dragging = false;
        private float lastMouseX = 0f;
        private float lastMouseY = 0f;
        private float rotationX = 0f;
        private float rotationY = 0f;

        public SimpleCubeRenderer(Vector2 screenCoord) {
                this.screenOrigin = screenCoord;

                camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                camera.near = -500f;
                camera.far = 500f;

                modelBatch = new ModelBatch();

                environment = new Environment();
                environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
                environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));
        }

        public void update() {
                float mouseX = Gdx.input.getX();
                float mouseY = Gdx.input.getY(); // Don't flip Y here

                if (Gdx.input.justTouched()) {
                        float halfSize = scale * 0.5f;

                        float cubeLeft = screenOrigin.x - halfSize;
                        float cubeRight = screenOrigin.x + halfSize;
                        float cubeTop = screenOrigin.y - halfSize; // Because origin is top-left, top is smaller Y
                        float cubeBottom = screenOrigin.y + halfSize;

                        if (mouseX >= cubeLeft && mouseX <= cubeRight &&
                                        mouseY >= cubeTop && mouseY <= cubeBottom) {

                                dragging = true;
                                lastMouseX = mouseX;
                                lastMouseY = mouseY;
                        }
                }

                if (dragging && Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
                        float deltaX = mouseX - lastMouseX;
                        float deltaY = mouseY - lastMouseY;

                        rotationY += deltaX * 0.5f;
                        rotationX += deltaY * 0.5f;

                        lastMouseX = mouseX;
                        lastMouseY = mouseY;
                } else if (!Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
                        dragging = false;
                }
        }

        public void render(float delta) {
                if (!built)
                        return;

                update(); // Update input and rotation

                camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Top-left origin
                camera.update();

                cubeInstance.transform.idt();
                cubeInstance.transform.setToTranslation(screenOrigin.x, screenOrigin.y, 0);
                cubeInstance.transform.rotate(Vector3.X, rotationX);
                cubeInstance.transform.rotate(Vector3.Y, rotationY);
                cubeInstance.transform.scale(scale, scale, scale);

                modelBatch.begin(camera);
                modelBatch.render(cubeInstance, environment);
                modelBatch.end();
        }

        public void dispose() {
                if (cubeModel != null)
                        cubeModel.dispose();
                if (modelBatch != null)
                        modelBatch.dispose();
        }

        public boolean isDragging() {
                return dragging;
        }

        public void setTopFace(TextureRegion texture) {
                this.topFace = texture;
                updateMaterialTexture(0, texture);
        }

        public void setBottomFace(TextureRegion texture) {
                this.bottomFace = texture;
                updateMaterialTexture(1, texture);
        }

        public void setSideFace(int index, TextureRegion texture) {
                if (index >= 0 && index < 4) {
                        sideFaces[index] = texture;
                        updateMaterialTexture(index + 2, texture);
                }
        }

        private void updateMaterialTexture(int faceIndex, TextureRegion textureRegion) {
                if (textureRegion == null)
                        return;

                // Update the reference
                if (faceIndex == 0)
                        topFace = textureRegion;
                else if (faceIndex == 1)
                        bottomFace = textureRegion;
                else if (faceIndex >= 2 && faceIndex < 6)
                        sideFaces[faceIndex - 2] = textureRegion;

                // Force rebuild
                dispose(); // Clean up old model
                built = false;
                buildCube();
        }

        public void buildCube() {
                if (built)
                        return;

                ModelBuilder builder = new ModelBuilder();
                builder.begin();

                // Correct top and bottom materials
                faceMaterials[1] = topFace != null
                                ? new Material(TextureAttribute.createDiffuse(topFace.getTexture()))
                                : new Material();
                faceMaterials[0] = bottomFace != null
                                ? new Material(TextureAttribute.createDiffuse(bottomFace.getTexture()))
                                : new Material();
                for (int i = 0; i < 4; i++) {
                        faceMaterials[i + 2] = sideFaces[i] != null
                                        ? new Material(TextureAttribute.createDiffuse(sideFaces[i].getTexture()))
                                        : new Material();
                }

                // Top face (+Y)
                MeshPartBuilder top = builder.part("top", GL20.GL_TRIANGLES,
                                Usage.Position | Usage.Normal | Usage.TextureCoordinates, faceMaterials[0]);
                top.rect(
                                new Vector3(+0.5f, +0.5f, +0.5f),
                                new Vector3(-0.5f, +0.5f, +0.5f),
                                new Vector3(-0.5f, +0.5f, -0.5f),
                                new Vector3(+0.5f, +0.5f, -0.5f),
                                new Vector3(0, 1, 0));

                // Bottom face (-Y)
                MeshPartBuilder bottom = builder.part("bottom", GL20.GL_TRIANGLES,
                                Usage.Position | Usage.Normal | Usage.TextureCoordinates, faceMaterials[1]);
                bottom.rect(
                                new Vector3(+0.5f, -0.5f, -0.5f),
                                new Vector3(-0.5f, -0.5f, -0.5f),
                                new Vector3(-0.5f, -0.5f, +0.5f),
                                new Vector3(+0.5f, -0.5f, +0.5f),
                                new Vector3(0, -1, 0));

                // Side 1 (-X) — rotated +90°
                MeshPartBuilder side1 = builder.part("side1", GL20.GL_TRIANGLES,
                                Usage.Position | Usage.Normal | Usage.TextureCoordinates, faceMaterials[2]);
                side1.rect(
                                new Vector3(-0.5f, +0.5f, -0.5f),
                                new Vector3(-0.5f, +0.5f, +0.5f),
                                new Vector3(-0.5f, -0.5f, +0.5f),
                                new Vector3(-0.5f, -0.5f, -0.5f),
                                new Vector3(-1, 0, 0));

                // Side 2 (+X) — rotated +90°
                MeshPartBuilder side2 = builder.part("side2", GL20.GL_TRIANGLES,
                                Usage.Position | Usage.Normal | Usage.TextureCoordinates, faceMaterials[3]);
                side2.rect(
                                new Vector3(+0.5f, +0.5f, +0.5f),
                                new Vector3(+0.5f, +0.5f, -0.5f),
                                new Vector3(+0.5f, -0.5f, -0.5f),
                                new Vector3(+0.5f, -0.5f, +0.5f),
                                new Vector3(1, 0, 0));

                // Side 3 (+Z) — rotated +90°
                MeshPartBuilder side3 = builder.part("side3", GL20.GL_TRIANGLES,
                                Usage.Position | Usage.Normal | Usage.TextureCoordinates, faceMaterials[4]);
                side3.rect(
                                new Vector3(-0.5f, +0.5f, +0.5f),
                                new Vector3(+0.5f, +0.5f, +0.5f),
                                new Vector3(+0.5f, -0.5f, +0.5f),
                                new Vector3(-0.5f, -0.5f, +0.5f),
                                new Vector3(0, 0, 1));

                // Side 4 (-Z) — rotated +90°
                MeshPartBuilder side4 = builder.part("side4", GL20.GL_TRIANGLES,
                                Usage.Position | Usage.Normal | Usage.TextureCoordinates, faceMaterials[5]);
                side4.rect(
                                new Vector3(+0.5f, +0.5f, -0.5f),
                                new Vector3(-0.5f, +0.5f, -0.5f),
                                new Vector3(-0.5f, -0.5f, -0.5f),
                                new Vector3(+0.5f, -0.5f, -0.5f),
                                new Vector3(0, 0, -1));

                cubeModel = builder.end();
                cubeInstance = new ModelInstance(cubeModel);
                built = true;
        }

}
