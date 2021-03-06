from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class UserHandler(RequestHandler):
	def get(self, id):
		print id
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

		self.write(json.dumps(user))
		self.finish()