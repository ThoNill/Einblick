package einblick;

public class EinblickException extends RuntimeException {

	private static final long serialVersionUID = -633945221778216360L;

	public EinblickException() {
		super();
	}

	public EinblickException(String message) {
		super(message);

	}

	public EinblickException(Throwable cause) {
		super(cause);

	}

	public EinblickException(String message, Throwable cause) {
		super(message, cause);

	}

	public EinblickException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
