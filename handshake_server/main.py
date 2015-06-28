import pycps
from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
from user_handler import UserHandler
from user_sign_up_handler import UserSignUpHandler
from session_handler import SessionHandler
from multi_user_handler import MultiUserHandler

class MainHandler(RequestHandler):
	def get(self):
		self.write("Hello handshake")

def make_app():
	return Application([
		url(r"/", MainHandler),
		url(r"/user/([0-9]+)/profile", UserHandler),
		url(r"/user/([0-9]+)/connections", MultiUserHandler),
		url(r"/user/signup", UserSignUpHandler),
		url(r"/session", SessionHandler),
	])
 
def main():
	app = make_app()
	app.listen(3000)
	IOLoop.current().start()

if __name__ == '__main__':
    main()