import pycps
from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
from user_handler import UserHandler
from user_sign_up_handler import UserSignUpHandler
from multi_user_handler import MultiUserHandler
# from connection_handler import ConnectionRegisterHandler

class MainHandler(RequestHandler):
	def get(self):
		self.write("Hello handshake")

def make_app():
	return Application([
		url(r"/", MainHandler),
		url(r"/user/([0-9]+)/profile", UserHandler),
		url(r"/user/([0-9]+)/connections", MultiUserHandler),
		url(r"/user/signup", UserSignUpHandler),
		# url(r"/connection/register", ConnectionRegisterHandler),
	])
 
def main():
	app = make_app()
	port = int(os.environ.get("PORT", 3000))
    app.listen(port)
	IOLoop.current().start()

if __name__ == '__main__':
    main()