package Application;

public class sceneLoaderController extends Main{
    public void switchToLoginPage() throws Exception {
        try{
            changeScene("/Login-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToRegisterPage() throws Exception {
        try{
            changeScene("/Register-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToHomePage() throws Exception {
        try{
            changeScene("/Home-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToMapPage() throws Exception {
        try{
            changeScene("/Map-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToGoalsPage() throws Exception {
        try{
            changeScene("/Goals-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void switchToCalendarPage() throws Exception {
        try{
            changeScene("/Calendar-Page.fxml");
            closeActiveStage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
