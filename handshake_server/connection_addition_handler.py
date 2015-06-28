from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class ConnectionAdditionHandler(RequestHandler):
	def post(self):
		user_id = self.get_argument('user_id')
		print user_id
		
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')
		try:
			response = con.retrieve(id)
		except pycps.APIError as e:
			print(e)
			if e.code == 2824:
				print("Requested non-existing id(s): {0}".format(', '.join(e.document_id)))
			return
		try:
			if response.get_documents:
				print("Found {0} documents: ".format(response.found))
				user = response.get_documents().items()[0]

		except Exception as e:
			print(e)
			return
		print user
		self.write(json.dumps(user))
		self.finish()